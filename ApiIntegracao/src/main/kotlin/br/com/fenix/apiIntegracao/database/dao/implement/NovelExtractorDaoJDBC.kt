package br.com.fenix.apiintegracao.database.dao.implement

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeResultSet
import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeStatement
import br.com.fenix.apiintegracao.database.dao.NovelExtractorDao
import br.com.fenix.apiintegracao.database.dao.PageBase
import br.com.fenix.apiintegracao.database.dao.implement.MangaExtractorDaoJDBC.Companion
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.messages.Mensagens
import br.com.fenix.apiintegracao.model.novelextractor.*
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.*
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

class NovelExtractorDaoJDBC(private val conn: Connection, private val base: String) : NovelExtractorDao, PageBase() {

    companion object {
        private val oLog = LoggerFactory.getLogger(NovelExtractorDaoJDBC::class.java)

        private const val CREATE_TABELA = "CALL create_table('%s');"
        private const val DROP_TABELA = "CALL drop_table('%s');"

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

        private const val EXIST_TABELA_VOCABULARIO = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%_vocabulario%%' AND Table_Name LIKE '%%%s%%' GROUP BY Tabela ")

        private const val SELECT_LISTA_TABELAS = ("SELECT REPLACE(Table_Name, '_volumes', '') AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%_volumes%%' GROUP BY Tabela ")

        private const val UPDATE_VOLUMES = "UPDATE %s_volumes SET novel = ?, titulo = ?, titulo_alternativo = ?, serie = ?, descricao = ?, autor = ?, editora = ?, volume = ?, linguagem = ?, arquivo = ?, is_processado = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_CAPITULOS = "UPDATE %s_capitulos SET novel = ?, volume = ?, capitulo = ?, descricao = ?, sequencia = ?, linguagem = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_TEXTO = "UPDATE %s_textos SET sequencia = ?, texto = ?, atualizacao = ? WHERE id = ?"
        private const val UPDATE_CAPA = "UPDATE %s_capas SET novel = ?, volume = ?, linguagem = ?, arquivo = ?, extensao = ?, capa = ?, atualizacao = ? WHERE id = ?"

        private const val INSERT_VOLUMES = "INSERT INTO %s_volumes (id, novel, titulo, titulo_alternativo, serie, descricao, autor, editora, volume, linguagem, arquivo, is_processado, atualizacao) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
        private const val INSERT_CAPITULOS = "INSERT INTO %s_capitulos (id, id_volume, novel, volume, capitulo, descricao, sequencia, linguagem, atualizacao) VALUES (?,?,?,?,?,?,?,?,?)"
        private const val INSERT_TEXTO = "INSERT INTO %s_textos (id, id_capitulo, sequencia, texto, atualizacao) VALUES (?,?,?,?,?)"
        private const val INSERT_CAPA = "INSERT INTO %s_capas (id, id_volume, novel, volume, linguagem, arquivo, extensao, capa, atualizacao) VALUES (?,?,?,?,?,?,?,?,?)"

        private const val DELETE_VOLUMES = "DELETE v FROM %s_volumes AS v %s"
        private const val DELETE_CAPITULOS = "DELETE c FROM %s_capitulos AS c INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s"
        private const val DELETE_PAGINAS = ("DELETE p FROM %s_paginas p "
                + "INNER JOIN %s_capitulos AS c ON c.id = p.id_capitulo INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s")
        private const val DELETE_TEXTOS = ("DELETE t FROM %s_textos AS t INNER JOIN %s_capitulos AS c ON c.id = t.id_capitulo INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s")
        private const val DELETE_CAPAS = "DELETE FROM %s_capas WHERE id_volume = ?"
        private const val DELETE_CAPA = "DELETE FROM %s_capas WHERE id = ?"

        private const val SELECT_VOLUMES = "SELECT id, novel, titulo, titulo_alternativo, serie, descricao, editora, autor, volume, linguagem, arquivo, is_favorito, is_processado, atualizacao FROM %s_volumes"
        private const val SELECT_CAPITULOS = "SELECT id, novel, volume, capitulo, descricao, sequencia, linguagem, atualizacao FROM %s_capitulos WHERE id_volume = ?"
        private const val SELECT_TEXTOS = "SELECT id, sequencia, texto, atualizacao FROM %s_textos WHERE id_capitulo = ? "
        private const val SELECT_CAPA = "SELECT id, novel, volume, linguagem, arquivo, extensao, capa, atualizacao FROM %s_capas WHERE id_volume = ? "

        private const val SELECT_VOLUME = "SELECT id, novel, titulo, titulo_alternativo, serie, descricao, editora, autor, volume, linguagem, arquivo, is_favorito, is_processado, atualizacao FROM %s_volumes WHERE id = ?"
        private const val SELECT_CAPITULO = "SELECT id, novel, volume, capitulo, descricao, sequencia, linguagem, atualizacao FROM %s_capitulos WHERE id = ?"
        private const val SELECT_TEXTO = "SELECT id, sequencia, texto, atualizacao FROM %s_textos WHERE id = ?"

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
    private fun getVolume(rs: ResultSet, base: String): NovelVolume {
        val id = UUID.fromString(rs.getString("id"))
        return NovelVolume(
            id,
            rs.getString("novel"),
            rs.getString("titulo"),
            rs.getString("titulo_alternativo"),
            rs.getString("serie") ?: "",
            rs.getString("descricao"),
            rs.getString("arquivo"),
            rs.getString("editora"),
            rs.getString("autor") ?: "",
            rs.getFloat("volume"),
            Linguagens.getEnum(rs.getString("linguagem"))!!,
            rs.getBoolean("is_favorito"),
            rs.getBoolean("is_processado"),
            selectCapa(base, id).orElse(null),
            selectAllCapitulos(base, id).toMutableList(),
            selectVocabulario(base, idVolume = id),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    private fun getCapitulo(rs: ResultSet, base: String): NovelCapitulo {
        val id = UUID.fromString(rs.getString("id"))
        return NovelCapitulo(
            id,
            rs.getString("novel"),
            rs.getFloat("volume"),
            rs.getFloat("capitulo"),
            rs.getString("descricao"),
            rs.getInt("sequencia"),
            Linguagens.getEnum(rs.getString("linguagem"))!!,
            selectAllTextos(base, id).toMutableList(),
            selectVocabulario(base, idCapitulo = id),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    private fun getTexto(rs: ResultSet): NovelTexto {
        return NovelTexto(
            UUID.fromString(rs.getString("id")),
            rs.getString("texto"),
            rs.getInt("sequencia"),
            rs.getObject("atualizacao", LocalDateTime::class.java)
        )
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun updateVolume(base: String, obj: NovelVolume): NovelVolume {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_VOLUMES, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.novel)
            st.setString(++index, obj.titulo)
            st.setString(++index, obj.tituloAlternativo)
            st.setString(++index, obj.serie)
            st.setString(++index, obj.descricao)
            st.setString(++index, obj.autor)
            st.setString(++index, obj.editora)
            st.setFloat(++index, obj.volume)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapa(base: String, obj: NovelCapa): NovelCapa {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_CAPA, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.novel)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.arquivo)
            st.setString(++index, obj.extenssao)
            st.setBytes(++index, obj.imagem)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId().toString())

            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
            return obj
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapitulo(base: String, obj: NovelCapitulo): NovelCapitulo {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_CAPITULOS, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.novel)
            st.setFloat(++index, obj.volume)
            st.setFloat(++index, obj.capitulo)
            st.setString(++index, obj.descricao)
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.lingua.sigla)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateTexto(base: String, obj: NovelTexto): NovelTexto {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.texto)
            st.setObject(++index, obj.atualizacao)
            st.setString(++index, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()

            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
            return obj
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun insertVolume(base: String, obj: NovelVolume): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_VOLUMES, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, obj.novel)
            st.setString(++index, obj.titulo)
            st.setString(++index, obj.tituloAlternativo)
            st.setString(++index, obj.serie)
            st.setString(++index, obj.descricao)
            st.setString(++index, obj.autor)
            st.setString(++index, obj.editora)
            st.setFloat(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertCapa(base: String, idVolume: UUID, obj: NovelCapa): UUID?  {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_CAPA, base), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setString(++index, obj.getId().toString())
            st.setString(++index, idVolume.toString())
            st.setString(++index, obj.novel)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.lingua.sigla)
            st.setString(++index, obj.arquivo)
            st.setString(++index, obj.extenssao)
            st.setBytes(++index, obj.imagem)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertCapitulo(base: String, idVolume: UUID, obj: NovelCapitulo): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT_CAPITULOS, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idVolume.toString())
            st.setString(++index, obj.novel)
            st.setFloat(++index, obj.volume)
            st.setFloat(++index, obj.capitulo)
            st.setString(++index, obj.descricao)
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.lingua.sigla)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertTexto(base: String, idCapitulo: UUID, obj: NovelTexto): UUID? {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(INSERT_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idCapitulo.toString())
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.texto)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
        return null
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun selectVolume(base: String, id: UUID?): Optional<NovelVolume> {
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectCapa(base: String, id: UUID?): Optional<NovelCapa> {
        require(id != null) { "O ID não pode ser nulo para a operação." }

        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPA, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next()) {
                Optional.of(NovelCapa(
                    UUID.fromString(rs.getString("id")), rs.getString("Novel"), rs.getInt("volume"),
                    Linguagens.getEnum(rs.getString("linguagem"))!!, rs.getString("arquivo"), rs.getString("extensao"),
                    rs.getBinaryStream("capa").readAllBytes(), rs.getObject("atualizacao", LocalDateTime::class.java)
                ))
            }else
                Optional.empty()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectCapitulo(base: String, id: UUID?): Optional<NovelCapitulo> {
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectTexto(base: String, id: UUID?): Optional<NovelTexto> {
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun selectAllVolumes(base: String): List<NovelVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base))
            rs = st.executeQuery()
            val list: MutableList<NovelVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))
            list
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, pageable: Pageable): Page<NovelVolume> {
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
            val list: MutableList<NovelVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime): List<NovelVolume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + WHERE_DATE_SYNC)
            st.setTimestamp(1, Timestamp.valueOf(dateTime))
            rs = st.executeQuery()
            val list: MutableList<NovelVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))
            list
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<NovelVolume> {
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
            val list: MutableList<NovelVolume> = mutableListOf()
            while (rs.next())
                list.add(getVolume(rs, base))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllCapitulos(base: String, idVolume: UUID): List<NovelCapitulo> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPITULOS, base))
            st.setString(1, idVolume.toString())
            rs = st.executeQuery()
            val list: MutableList<NovelCapitulo> = mutableListOf()
            while (rs.next())
                list.add(getCapitulo(rs, base))
            list
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAllTextos(base: String, idCapitulo: UUID): List<NovelTexto> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_TEXTOS, base))
            st.setString(1, idCapitulo.toString())
            rs = st.executeQuery()
            val list: MutableList<NovelTexto> = mutableListOf()
            while (rs.next())
                list.add(getTexto(rs))
            list
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //
    override fun deleteVolume(base: String, obj: NovelVolume) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_VOLUMES, base))
            st.setString(1, obj.getId().toString())
            conn.autoCommit = false
            conn.beginRequest()

            deleteCapa(base, obj.getId()!!)
            deleteVocabulario(base, idVolume = obj.getId())

            for (capitulo in obj.capitulos)
                deleteCapitulo(base, capitulo, false)

            st.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(st)
        }
    }

    private fun deleteCapa(base: String, idVolume: UUID) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_CAPAS, base))
            st.setString(1, idVolume.toString())
            st.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            closeStatement(st)
        }
    }

    override fun deleteCapa(base: String, obj: NovelCapa, transaction: Boolean) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_CAPA, base))
            st.setString(1, obj.getId().toString())

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
                e1.printStackTrace()
            }
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                if (transaction)
                    conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(st)
        }
    }

    override fun deleteCapitulo(base: String, obj: NovelCapitulo, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_CAPITULOS, base))
            st.setString(1, obj.getId().toString())

            if (transaction) {
                conn.autoCommit = false
                conn.beginRequest()
            }

            deleteVocabulario(base, idCapitulo = obj.getId())

            for (texto in obj.textos)
                deleteTexto(base, texto, false)

            st.executeUpdate()

            if (transaction)
                conn.commit()

        } catch (e: SQLException) {
            try {
                if (transaction)
                    conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                if (transaction)
                    conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(st)
        }
    }

    override fun deleteTexto(base: String, obj: NovelTexto, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_TEXTOS, base))
            st.setString(1, obj.getId().toString())

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
                e1.printStackTrace()
            }
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                if (transaction)
                    conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
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
            return rs.next()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
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
                e.printStackTrace()
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
            } finally {
                closeStatement(st)
                closeResultSet(rs)
            }
        }

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(SQLException::class)
    override fun selectVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?): MutableSet<NovelVocabulario> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            val campo = when {
                idVolume != null -> "id_volume"
                idCapitulo != null -> "id_capitulo"
                else -> ""
            }

            val valor = when {
                idVolume != null -> idVolume.toString()
                idCapitulo != null -> idCapitulo.toString()
                else -> ""
            }

            st = conn.prepareStatement(String.format(SELECT_VOCABUALARIO, base, campo))
            st.setString(1, valor)
            rs = st.executeQuery()
            val list: MutableSet<NovelVocabulario> = mutableSetOf()

            while (rs.next())
                list.add(NovelVocabulario(
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
    override fun insertVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, vocabulario: Set<NovelVocabulario>) {
        var st: PreparedStatement? = null
        try {
            if (idVolume == null && idCapitulo == null)
                return

            val campo = if (idVolume != null) "id_volume" else "id_capitulo"
            val id = idVolume ?: idCapitulo
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    @Throws(SQLException::class)
    override fun deleteVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            val campo = when {
                idVolume != null -> "id_volume"
                idCapitulo != null -> "id_capitulo"
                else -> ""
            }

            val valor = when {
                idVolume != null -> idVolume.toString()
                idCapitulo != null -> idCapitulo.toString()
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
                e1.printStackTrace()
            }
            println(st.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                if (transaction)
                    conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(st)
        }
    }
}