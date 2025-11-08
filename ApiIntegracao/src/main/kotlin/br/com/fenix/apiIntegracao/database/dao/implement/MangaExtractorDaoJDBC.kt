package br.com.fenix.apiintegracao.database.dao.implement

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeResultSet
import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeStatement
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.database.dao.PageBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.messages.Mensagens
import br.com.fenix.apiintegracao.model.mangaextractor.*
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.*
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

class MangaExtractorDaoJDBC(private val conn: Connection, private val base: String) : MangaExtractorDao, PageBase() {

    companion object {
        private val oLog = LoggerFactory.getLogger(MangaExtractorDaoJDBC::class.java)

        private const val CREATE_TABELA = "CALL sp_create_table('%s');"
        private const val DROP_TABELA = "CALL sp_drop_table('%s');"

        private const val TABELA_VOLUME = "_volumes"
        private const val TABELA_CAPITULO = "_capitulos"
        private const val TABELA_PAGINA = "_paginas"
        private const val TABELA_TEXTO = "_textos"
        private const val TABELA_VOCABULARIO = "_vocabularios"
        private const val TABELA_CAPA = "_capas"

        private const val CREATE_TRIGGER_INSERT = "CREATE TRIGGER tr_%s_insert BEFORE INSERT ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    IF (NEW.id IS NULL OR NEW.id = '') THEN" +
                "      SET new.id = UUID();" +
                "    END IF;" +
                "  END"
        private const val CREATE_TRIGGER_UPDATE = "CREATE TRIGGER tr_%s_update BEFORE UPDATE ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    SET new.Atualizacao = NOW();" +
                "  END"

        private const val EXIST_TABELA_VOCABULARIO = "SELECT fn_vocabulary_exists('%s', '%s')"
        private const val SELECT_LISTA_TABELAS = "CALL sp_list_tables('%s')"

        private const val UPDATE_VOLUMES = "UPDATE %s_volumes SET manga = ?, volume = ?, linguagem = ?, arquivo = ?, is_processado = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_CAPITULOS = "UPDATE %s_capitulos SET manga = ?, volume = ?, capitulo = ?, linguagem = ?, scan = ?, is_extra = ?, is_raw = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_PAGINAS = "UPDATE %s_paginas SET nome = ?, numero = ?, hash_pagina = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_TEXTO = "UPDATE %s_textos SET sequencia = ?, texto = ?, posicao_x1 = ?, posicao_y1 = ?, posicao_x2 = ?, posicao_y2 = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_CAPA = "UPDATE %s_capas SET manga = ?, volume = ?, linguagem = ?, arquivo = ?, extensao = ?, capa = ?, atualizacao = ? WHERE id = ?"

        private const val INSERT_VOLUMES = "INSERT INTO %s_volumes (id, manga, volume, linguagem, arquivo, is_processado, atualizacao) VALUES (?,?,?,?,?,?,?)"
        private const val INSERT_CAPITULOS = "INSERT INTO %s_capitulos (id, id_volume, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, atualizacao) VALUES (?,?,?,?,?,?,?,?,?,?)"
        private const val INSERT_PAGINAS = "INSERT INTO %s_paginas (id, id_capitulo, nome, numero, hash_pagina, atualizacao) VALUES (?,?,?,?,?,?)"
        private const val INSERT_TEXTO = "INSERT INTO %s_textos (id, id_pagina, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2, atualizacao) VALUES (?,?,?,?,?,?,?,?,?)"
        private const val INSERT_CAPA = "INSERT INTO %s_capas (id, id_volume, manga, volume, linguagem, arquivo, extensao, capa, atualizacao) VALUES (?,?,?,?,?,?,?,?,?)"

        private const val DELETE_VOLUMES = "CALL sp_delete_volume('%s', '%s')"
        private const val DELETE_CAPITULOS = "CALL sp_delete_capitulos('%s', '%s')"

        private const val SELECT_VOLUMES = "SELECT id, manga, volume, linguagem, arquivo, is_processado, atualizacao FROM %s_volumes"
        private const val SELECT_CAPITULOS = "SELECT id, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, atualizacao FROM %s_capitulos WHERE id_volume = ?"
        private const val SELECT_PAGINAS = "SELECT id, nome, numero, hash_pagina, atualizacao FROM %s_paginas WHERE id_capitulo = ? "
        private const val SELECT_TEXTOS = "SELECT id, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2, atualizacao FROM %s_textos WHERE id_pagina = ? "
        private const val SELECT_CAPA = "SELECT id, manga, volume, linguagem, arquivo, extensao, capa, atualizacao FROM %s_capas WHERE id_volume = ? "

        private const val SELECT_VOLUME = "SELECT id, manga, volume, linguagem, arquivo, is_processado, atualizacao FROM %s_volumes WHERE id = ?"
        private const val SELECT_CAPITULO = "SELECT id, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, atualizacao FROM %s_capitulos WHERE id = ?"
        private const val SELECT_PAGINA = "SELECT id, nome, numero, hash_pagina, atualizacao FROM %s_paginas WHERE id = ?"
        private const val SELECT_TEXTO = "SELECT id, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2, atualizacao FROM %s_textos WHERE id = ?"

        private const val SELECT_VOCABUALARIO = "SELECT id, palavra, portugues, ingles, leitura, revisado, d.atualizacao FROM %s_vocabularios V INNER JOIN _vocabularios D ON V.id_vocabulario = D.id WHERE V.%s = ?;"
        private const val DELETE_VOCABULARIO = "DELETE FROM %s_vocabularios WHERE %s = ?;"
        private const val INSERT_VOCABULARIO = "INSERT INTO %s_vocabularios (%s, id_vocabulario) VALUES (?,?);"
        private const val INSERT_VOCABULARIOS = "INSERT IGNORE INTO _vocabularios (id, palavra, portugues, ingles, leitura, revisado, atualizacao) VALUES (?,?,?,?,?,?,?);"

        private const val SELECT_COUNT_VOLUMES = "SELECT count(*) as total FROM %s_volumes"

        private const val WHERE_DATE_SYNC = " WHERE atualizacao >= ?"
        private const val ORDER_BY = " ORDER BY %s"
        private const val LIMIT = " LIMIT %s OFFSET %s"
    }

    // -------------------------------------------------------------------------------------------------------------  //
    private fun getVolume(rs: ResultSet, base: String): MangaVolume {
        val id = UUID.fromString(rs.getString("id"))
        return MangaVolume(
            id,
            rs.getString("manga"),
            rs.getInt("volume"),
            Linguagens.getEnum(rs.getString("linguagem"))!!,
            selectAllCapitulos(base, id).toMutableList(),
            selectVocabulario(base, idVolume = id),
            rs.getString("arquivo") ?: "",
            rs.getBoolean("is_processado"),
            selectCapa(base, id).orElse(null),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    private fun getCapitulo(rs: ResultSet, base: String): MangaCapitulo {
        val id = UUID.fromString(rs.getString("id"))
        return MangaCapitulo(
            id,
            rs.getString("manga"),
            rs.getInt("volume"),
            rs.getFloat("capitulo"),
            Linguagens.getEnum(rs.getString("linguagem"))!!,
            rs.getString("scan"),
            selectAllPaginas(base, id).toMutableList(),
            rs.getBoolean("is_extra"),
            rs.getBoolean("is_raw"),
            selectVocabulario(base, idCapitulo = id),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    private fun getPagina(rs: ResultSet, base: String): MangaPagina {
        val id = UUID.fromString(rs.getString("id"))
        return MangaPagina(
            id,
            rs.getString("nome"),
            rs.getInt("numero"),
            rs.getString("hash_pagina"),
            selectAllTextos(base, id).toMutableList(),
            selectVocabulario(base, idPagina = id),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    private fun getTexto(rs: ResultSet): MangaTexto {
        return MangaTexto(
            UUID.fromString(rs.getString("id")),
            rs.getString("texto"),
            rs.getInt("sequencia"),
            rs.getInt("posicao_x1"),
            rs.getInt("posicao_y1"),
            rs.getInt("posicao_x2"),
            rs.getInt("posicao_y2"),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun updateVolume(base: String, obj: MangaVolume): MangaVolume {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_VOLUMES, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.arquivo)
            st.setBoolean(++index, obj.processado)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()

            obj.capa?.let { updateCapa(base, it) }
            deleteVocabulario(base, idVolume = obj.getId())
            insertVocabulario(base, idVolume = obj.getId(), vocabulario = obj.vocabularios)

            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
            return obj
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapa(base: String, obj: MangaCapa): MangaCapa {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_CAPA, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.arquivo)
            st.setString(++index, obj.extenssao)

            val baos = ByteArrayOutputStream()
            ImageIO.write(obj.imagem, obj.extenssao, baos)
            st.setBinaryStream(++index, ByteArrayInputStream(baos.toByteArray()))

            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId().toString())

            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
            return obj
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapitulo(base: String, obj: MangaCapitulo): MangaCapitulo {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_CAPITULOS, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setFloat(++index, obj.capitulo)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.scan)
            st.setBoolean(++index, obj.isExtra)
            st.setBoolean(++index, obj.isRaw)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()

            deleteVocabulario(base, idCapitulo = obj.getId())
            insertVocabulario(base, idCapitulo = obj.getId(), vocabulario = obj.vocabularios)

            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
            return obj
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updatePagina(base: String, obj: MangaPagina): MangaPagina {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_PAGINAS, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.nomePagina)
            st.setInt(++index, obj.numero)
            st.setString(++index, obj.hash)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()

            deleteVocabulario(base, idPagina = obj.getId())
            insertVocabulario(base, idPagina = obj.getId(), vocabulario = obj.vocabularios)

            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
            return obj
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateTexto(base: String, obj: MangaTexto): MangaTexto {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.texto)
            st.setInt(++index, obj.x1)
            st.setInt(++index, obj.y1)
            st.setInt(++index, obj.x2)
            st.setInt(++index, obj.y2)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
            return obj
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun insertVolume(base: String, obj: MangaVolume): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_VOLUMES, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua!!.sigla)
            st.setString(++index, obj.arquivo)
            st.setBoolean(++index, obj.processado)
            st.setObject(++index, obj.atualizacao)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    obj.capa?.let { it.setId(insertCapa(base, id, it)) }
                    insertVocabulario(base, idVolume = id, vocabulario = obj.vocabularios)
                }
                id
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertCapa(base: String, idVolume: UUID, obj: MangaCapa): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_CAPA, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.getId().toString())
            st.setString(++index, idVolume.toString())
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.arquivo)
            st.setString(++index, obj.extenssao)

            val baos = ByteArrayOutputStream()
            ImageIO.write(obj.imagem, obj.extenssao, baos)
            st.setBinaryStream(++index, ByteArrayInputStream(baos.toByteArray()))

            st.setObject(++index, obj.atualizacao)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next())
                    id = UUID.fromString(rs.getString(1))
                id
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertCapitulo(base: String, idVolume: UUID, obj: MangaCapitulo): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_CAPITULOS, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idVolume.toString())
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setFloat(++index, obj.capitulo)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.scan)
            st.setBoolean(++index, obj.isExtra)
            st.setBoolean(++index, obj.isRaw)
            st.setObject(++index, obj.atualizacao)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    insertVocabulario(base, idCapitulo = id, vocabulario = obj.vocabularios)
                }
                id
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertPagina(base: String, idCapitulo: UUID, obj: MangaPagina): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_PAGINAS, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idCapitulo.toString())
            st.setString(++index, obj.nomePagina)
            st.setInt(++index, obj.numero)
            st.setString(++index, obj.hash)
            st.setObject(++index, obj.atualizacao)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    insertVocabulario(base, idPagina = id, vocabulario = obj.vocabularios)
                }
                id
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertTexto(base: String, idPagina: UUID, obj: MangaTexto): UUID? {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(INSERT_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idPagina.toString())
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.texto)
            st.setInt(++index, obj.x1)
            st.setInt(++index, obj.y1)
            st.setInt(++index, obj.x2)
            st.setInt(++index, obj.y2)
            st.setObject(++index, obj.atualizacao)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                if (rs.next())
                    return UUID.fromString(rs.getString(0))
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
        return null
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun selectVolume(base: String, id: UUID?): Optional<MangaVolume> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUME, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                Optional.of(getVolume(rs, base))
            else
                Optional.empty()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectCapa(base: String, id: UUID?): Optional<MangaCapa> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPA, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next()) {
                val input = ByteArrayInputStream(rs.getBinaryStream("capa").readAllBytes())
                val image: BufferedImage? = ImageIO.read(input)
                Optional.of(MangaCapa(
                    UUID.fromString(rs.getString("id")), rs.getString("manga"), rs.getInt("volume"),
                    Linguagens.getEnum(rs.getString("linguagem"))!!, rs.getString("arquivo"), rs.getString("extensao"),
                    image, rs.getObject("atualizacao", LocalDateTime::class.java)
                ))
            } else
                Optional.empty()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectCapitulo(base: String, id: UUID?): Optional<MangaCapitulo> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPITULO, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                Optional.of(getCapitulo(rs, base))
            else
                Optional.empty()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectPagina(base: String, id: UUID?): Optional<MangaPagina> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_PAGINA, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                Optional.of(getPagina(rs, base))
            else
                Optional.empty()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectTexto(base: String, id: UUID?): Optional<MangaTexto> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_TEXTO, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                Optional.of(getTexto(rs))
            else
                Optional.empty()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun selectAllVolumes(base: String): List<MangaVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base))
            rs = st.executeQuery()
            val list: MutableList<MangaVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, pageable: Pageable): Page<MangaVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_COUNT_VOLUMES, base))
            rs = st.executeQuery()
            var total = 1
            if (rs.next())
                total = rs.getInt(1)
            if (total < 1)
                total = 1

            var order = ""
            if (!pageable.sort.isEmpty) {
                for (sort in pageable.sort)
                    order += "${sort.property} ${if (sort.direction.isAscending) "ASC" else "DESC"}, "

                if (order.trim().isEmpty())
                    order = "1,"

                order = String.format(ORDER_BY, order.substringBeforeLast(","))
            }

            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + order + String.format(LIMIT, pageable.pageSize, pageable.pageNumber))
            rs = st.executeQuery()
            val list: MutableList<MangaVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime): List<MangaVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + WHERE_DATE_SYNC)
            st.setTimestamp(1, Timestamp.valueOf(dateTime))
            rs = st.executeQuery()
            val list: MutableList<MangaVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<MangaVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            val time = Timestamp.valueOf(dateTime)
            st = conn.prepareStatement(String.format(SELECT_COUNT_VOLUMES, base) + WHERE_DATE_SYNC)
            st.setTimestamp(1, time)
            rs = st.executeQuery()
            var total = 1
            if (rs.next())
                total = rs.getInt(1)
            if (total < 1)
                total = 1

            var order = ""
            if (!pageable.sort.isEmpty) {
                for (sort in pageable.sort)
                    order += "${sort.property} ${if (sort.direction.isAscending) "ASC" else "DESC"}, "

                if (order.trim().isEmpty())
                    order = "1,"

                order = String.format(ORDER_BY, order.substringBeforeLast(","))
            }

            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + WHERE_DATE_SYNC + order + String.format(LIMIT, pageable.pageSize, pageable.pageNumber) )
            st.setTimestamp(1, time)
            rs = st.executeQuery()
            val list: MutableList<MangaVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllCapitulos(base: String, idVolume: UUID): List<MangaCapitulo> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPITULOS, base))
            st.setString(1, idVolume.toString())
            rs = st.executeQuery()
            val list: MutableList<MangaCapitulo> = mutableListOf()
            while (rs.next())
                list.add(getCapitulo(rs, base))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllPaginas(base: String, idCapitulo: UUID): List<MangaPagina> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_PAGINAS, base))
            st.setString(1, idCapitulo.toString())
            rs = st.executeQuery()
            val list: MutableList<MangaPagina> = mutableListOf()
            while (rs.next())
                list.add(getPagina(rs, base))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllTextos(base: String, idPagina: UUID): List<MangaTexto> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_TEXTOS, base))
            st.setString(1, idPagina.toString())
            rs = st.executeQuery()
            val list: MutableList<MangaTexto> = mutableListOf()
            while (rs.next())
                list.add(getTexto(rs))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun deleteVolume(base: String, obj: MangaVolume) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_VOLUMES, base, obj.getId().toString()))
            st.executeUpdate()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            closeStatement(st)
        }
    }

    override fun deleteCapitulo(base: String, obj: MangaCapitulo) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_CAPITULOS, base, obj.getId().toString()))
            st.executeUpdate()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(ExceptionDb::class)
    private fun createTriggers(nome: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_INSERT, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(st.toString())
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_UPDATE, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(st.toString())
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    override fun createTable(nome: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(CREATE_TABELA, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(st.toString())
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        createTriggers(nome + TABELA_VOLUME)
        createTriggers(nome + TABELA_CAPITULO)
        createTriggers(nome + TABELA_PAGINA)
        createTriggers(nome + TABELA_TEXTO)
        createTriggers(nome + TABELA_CAPA)
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_UPDATE, nome + TABELA_VOCABULARIO, nome + TABELA_VOCABULARIO))
            st.execute()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(st.toString())
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    override fun existTable(nome: String): Boolean {
        var st: PreparedStatement? = null
        val rs: ResultSet?
        try {
            st = conn.prepareStatement(String.format(EXIST_TABELA_VOCABULARIO, base, nome))
            rs = st.executeQuery()
            return rs.next() && rs.getBoolean(1)
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    @get:Throws(ExceptionDb::class)
    override val tables: List<String>
        get() {
            var st: PreparedStatement? = null
            var rs: ResultSet? = null
            return try {
                st = conn.prepareStatement(String.format(SELECT_LISTA_TABELAS, base))
                rs = st.executeQuery()
                val list: MutableList<String> = ArrayList()
                while (rs.next()) list.add(rs.getString("Tabela"))
                list
            } catch (e: SQLException) {
                oLog.error("Error ao executar o comando: " + st.toString(), e)
                throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
            } finally {
                closeStatement(st)
                closeResultSet(rs)
            }
        }

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(SQLException::class)
    override fun selectVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?): MutableSet<MangaVocabulario> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            val campo = when {
                idVolume != null -> "id_volume"
                idCapitulo != null -> "id_capitulo"
                idPagina != null -> "id_pagina"
                else -> ""
            }

            val valor = when {
                idVolume != null -> idVolume.toString()
                idCapitulo != null -> idCapitulo.toString()
                idPagina != null -> idPagina.toString()
                else -> ""
            }

            st = conn.prepareStatement(String.format(SELECT_VOCABUALARIO, base, campo))
            st.setString(1, valor)
            rs = st.executeQuery()
            val list: MutableSet<MangaVocabulario> = mutableSetOf()

            while (rs.next())
                list.add(MangaVocabulario(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("palavra"),
                    rs.getString("leitura"),
                    rs.getString("ingles"),
                    rs.getString("portugues"),
                    rs.getBoolean("revisado"),
                    rs.getObject("atualizacao", LocalDateTime::class.java)
                ))
            list
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(st.toString())
            throw SQLException(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    @Throws(SQLException::class)
    override fun insertVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, vocabulario: Set<MangaVocabulario>) {
        var st: PreparedStatement? = null
        try {
            if (idVolume == null && idCapitulo == null && idPagina == null)
                return

            val campo = if (idVolume != null) "id_volume" else if (idCapitulo != null) "id_capitulo" else "id_pagina"
            val id = idVolume ?: (idCapitulo ?: idPagina)!!
            for (vocab in vocabulario) {
                st = conn.prepareStatement(INSERT_VOCABULARIOS)
                var index = 0
                st.setString(++index, vocab.getId().toString())
                st.setString(++index, vocab.palavra)
                st.setString(++index, vocab.portugues)
                st.setString(++index, vocab.ingles)
                st.setString(++index, vocab.leitura)
                st.setBoolean(++index, vocab.revisado)
                st.setObject(++index, vocab.atualizacao)
                st.executeUpdate()

                st = conn.prepareStatement(String.format(INSERT_VOCABULARIO, base, campo))
                st.setString(++index, id.toString())
                st.setString(++index, vocab.getId().toString())
                st.executeUpdate()
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    @Throws(SQLException::class)
    override fun deleteVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            val campo = when {
                idVolume != null -> "id_volume"
                idCapitulo != null -> "id_capitulo"
                idPagina != null -> "id_pagina"
                else -> ""
            }

            val valor = when {
                idVolume != null -> idVolume.toString()
                idCapitulo != null -> idCapitulo.toString()
                idPagina != null -> idPagina.toString()
                else -> ""
            }

            st = conn.prepareStatement(String.format(DELETE_VOCABULARIO, base, campo))
            st.setString(1, valor)

            if (transaction) {
                conn.autoCommit = false
                conn.beginRequest()
            }
            st.executeUpdate()

            if (transaction)
                conn.commit()
        } catch (e: SQLException) {
            try {
                if (transaction)
                    conn.rollback()
            } catch (e1: SQLException) {
                oLog.error(e1.message, e1)
            }
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                if (transaction)
                    conn.autoCommit = true
            } catch (e: SQLException) {
                oLog.error(e.message, e)
            }
            closeStatement(st)
        }
    }
}