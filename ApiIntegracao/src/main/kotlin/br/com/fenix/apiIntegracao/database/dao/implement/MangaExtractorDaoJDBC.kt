package br.com.fenix.apiIntegracao.database.dao.implement

import br.com.fenix.apiIntegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiIntegracao.database.mysql.DB.closeResultSet
import br.com.fenix.apiIntegracao.database.mysql.DB.closeStatement
import br.com.fenix.apiIntegracao.enums.Linguagens
import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.messages.Mensagens
import br.com.fenix.apiIntegracao.model.mangaextractor.*
import org.springframework.data.domain.*
import java.sql.*
import java.time.LocalDateTime
import java.util.*

class MangaExtractorDaoJDBC(private val conn: Connection, private val base: String) : MangaExtractorDao {

    companion object {
        private const val UPDATE_VOLUMES =
            "UPDATE %s_volumes SET manga = ?, volume = ?, linguagem = ?, arquivo = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_CAPITULOS =
            "UPDATE %s_capitulos SET manga = ?, volume = ?, capitulo = ?, linguagem = ?, is_extra = ?, scan = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_PAGINAS =
            "UPDATE %s_paginas SET nome = ?, numero = ?, hash_pagina = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_TEXTO =
            "UPDATE %s_textos SET sequencia = ?, texto = ?, posicao_x1 = ?, posicao_y1 = ?, posicao_x2 = ?, posicao_y2 = ? WHERE id = ?"

        private const val INSERT_VOLUMES =
            "INSERT INTO %s_volumes (id, manga, volume, linguagem, arquivo, is_processado) VALUES (?,?,?,?,?,?)"
        private const val INSERT_CAPITULOS =
            "INSERT INTO %s_capitulos (id, id_volume, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, is_processado) VALUES (?,?,?,?,?,?,?,?,?,?)"
        private const val INSERT_PAGINAS =
            "INSERT INTO %s_paginas (id, id_capitulo, nome, numero, hash_pagina, is_processado) VALUES (?,?,?,?,?,?)"
        private const val INSERT_TEXTO =
            "INSERT INTO %s_textos (id, id_pagina, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2) VALUES (?,?,?,?,?,?,?,?)"

        private const val DELETE_VOLUMES = "DELETE %s_volumes WHERE id = ?"
        private const val DELETE_CAPITULOS = "DELETE %s_capitulos WHERE id = ?"
        private const val DELETE_PAGINAS = "DELETE %s_paginas WHERE id = ?"
        private const val DELETE_TEXTOS = "DELETE %s_textos WHERE id = ?"

        private const val SELECT_VOLUMES = "SELECT id, manga, volume, linguagem, arquivo, is_Processado FROM %s_volumes"
        private const val SELECT_CAPITULOS = "SELECT id, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, is_processado FROM %s_capitulos CAP WHERE id_volume = ?"
        private const val SELECT_PAGINAS = "SELECT id, nome, numero, hash_pagina, is_processado FROM %s_paginas WHERE id_capitulo = ? "
        private const val SELECT_TEXTOS = "SELECT id, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2 FROM %s_textos WHERE id_pagina = ? "

        private const val SELECT_VOLUME = "SELECT VOL.id, VOL.manga, VOL.volume, VOL.linguagem, VOL.arquivo, VOL.is_Processado FROM %s_volumes VOL WHERE id = ?"
        private const val SELECT_CAPITULO = ("SELECT id, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, is_processado FROM %s_capitulos WHERE id = ?")
        private const val SELECT_PAGINA = "SELECT id, nome, numero, hash_pagina, is_processado FROM %s_paginas WHERE id = ?"
        private const val SELECT_TEXTO = "SELECT id, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2, versaoApp FROM %s_textos WHERE id = ?"

        private const val SELECT_COUNT_VOLUMES = "SELECT count(*) as total FROM %s_volumes"

        private const val WHERE_DATE_SYNC = " WHERE atualizacao >= ?"
        private const val ORDER_BY = " ORDER BY %s"
        private const val LIMIT = " LIMIT %s OFFSET %s"

        private const val DELETE_VOCABULARIO = "DELETE FROM %s_vocabulario WHERE %s = ?;"
        private const val INSERT_VOCABULARIO = ("INSERT INTO %s_vocabulario (%s, palavra, portugues, ingles, leitura, revisado) "
                    + " VALUES (?,?,?,?,?,?);")
        private const val SELECT_VOCABUALARIO = "SELECT id, id_volume, id_capitulo, id_pagina, palavra, portugues, ingles, leitura, revisado FROM %s_vocabulario WHERE %s "


        private const val CREATE_TABELA = "CALL create_table('%s');"
        private const val TABELA_VOLUME = "_volumes"
        private const val TABELA_CAPITULO = "_capitulos"
        private const val TABELA_PAGINA = "_paginas"
        private const val TABELA_TEXTO = "_textos"
        private const val TABELA_VOCABULARIO = "_vocabularios"


        private const val CREATE_TRIGGER_INSERT = "DELIMITER $$" +
                "CREATE TRIGGER tr_%s_insert BEFORE UPDATE ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    IF (NEW.id IS NULL OR NEW.id = '') THEN" +
                "      SET new.id = UUID();" +
                "    END IF;" +
                "  END$$" +
                "DELIMITER ;"

        private const val CREATE_TRIGGER_UPDATE = "DELIMITER $$" +
                "CREATE TRIGGER tr_%s_update BEFORE UPDATE ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    SET new.atualizacao = NOW();" +
                "  END$$" +
                "DELIMITER ;"

        private const val EXIST_TABELA_VOCABULARIO = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%_vocabulario%%' AND Table_Name LIKE '%%%s%%' GROUP BY Tabela ")

        private const val SELECT_LISTA_TABELAS = ("SELECT REPLACE(Table_Name, '_volumes', '') AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%_volumes%%' GROUP BY Tabela ")
    }

    // -------------------------------------------------------------------------------------------------------------  //
    private fun getVolume(rs: ResultSet, base: String): Volume {
        val id = UUID.fromString(rs.getString("id"))
        return Volume(
            id,
            rs.getString("manga"),
            rs.getInt("volume"),
            Linguagens.getEnum(rs.getString("linguagem")),
            rs.getString("arquivo"),
            rs.getString("scan"),
            rs.getBoolean("is_processado"),
            selectAllCapitulos(base, id),
            selectAllVocabularios(base, id, null, null, false)
        )
    }

    private fun getCapitulo(rs: ResultSet, base: String): Capitulo {
        val id = UUID.fromString(rs.getString("id"))
        return Capitulo(
            id,
            rs.getString("manga"),
            rs.getInt("volume"),
            rs.getDouble("capitulo"),
            Linguagens.getEnum(rs.getString("linguagem")),
            rs.getString("scan"),
            rs.getBoolean("is_extra"),
            rs.getBoolean("is_raw"),
            rs.getBoolean("is_processado"),
            selectAllPaginas(base, id),
            selectAllVocabularios(base, null, id, null, false)
        )
    }

    private fun getPagina(rs: ResultSet, base: String): Pagina {
        val id = UUID.fromString(rs.getString("id"))
        return Pagina(
            id,
            rs.getString("nome"),
            rs.getInt("numero"),
            rs.getString("nomePagina"),
            rs.getString("hash_pagina"),
            rs.getBoolean("is_processado"),
            selectAllTextos(base, id),
            selectAllVocabularios(base, null, null, id, false)
        )
    }

    private fun getTexto(rs: ResultSet): Texto {
        return Texto(
            UUID.fromString(rs.getString("id")),
            rs.getInt("sequencia"),
            rs.getString("texto"),
            rs.getInt("posicao_x1"),
            rs.getInt("posicao_x2"),
            rs.getInt("posicao_y1"),
            rs.getInt("posicao_y2"),
            rs.getInt("versaoApp")
        )
    }

    private fun getVocabulario(rs: ResultSet, base: String): Vocabulario {
        return Vocabulario(
            UUID.fromString(rs.getString("id")),
            rs.getString("palavra"),
            rs.getString("portugues"),
            rs.getString("ingles"),
            rs.getString("leitura"),
            rs.getBoolean("is_revisado"),
            (if (rs.getObject("id_volume") != null) selectVolume(base,  UUID.fromString(rs.getString("id_volume"))) else null),
            (if (rs.getObject("id_capitulo") != null) selectCapitulo(base, UUID.fromString(rs.getString("id_capitulo"))) else null),
            (if (rs.getObject("id_pagina") != null) selectPagina(base, UUID.fromString(rs.getString("id_pagina"))) else null)
        )
    }

    private fun <E> toPageable(pageable: Pageable, total: Int, list: List<E>) : Page<E> {
        val page: Pageable = object : Pageable {
            override fun getPageNumber(): Int {
                return pageable.pageNumber
            }

            override fun getPageSize(): Int {
                return pageable.pageSize
            }

            override fun getOffset(): Long {
                return pageable.pageNumber * pageable.pageSize.toLong()
            }

            override fun getSort(): Sort {
                return pageable.sort
            }

            override fun next(): Pageable {
                return PageRequest.of(this.pageNumber + 1, this.pageSize, this.sort)
            }

            override fun previousOrFirst(): Pageable {
                return if (this.pageNumber == 0) this else PageRequest.of(this.pageNumber - 1, this.pageSize, this.sort)
            }

            override fun first(): Pageable {
                return PageRequest.of(0, this.pageSize, this.sort)
            }

            override fun withPage(pageNumber: Int): Pageable {
                TODO("Not yet implemented")
            }

            override fun hasPrevious(): Boolean {
                return pageable.pageNumber > 0
            }
        }

        return PageImpl(list, page, total.toLong())
    }


    // -------------------------------------------------------------------------------------------------------------  //

    override fun updateVolume(base: String, obj: Volume) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_VOLUMES, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.manga)
            st.setInt(2, obj.volume)
            st.setString(3, obj.linguagem!!.sigla)
            st.setString(4, obj.arquivo)
            st.setBoolean(5, obj.isProcessado)
            st.setString(6, obj.getId()!!.toString())
            insertVocabulario(base, obj.getId(), null, null, obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapitulo(base: String, obj: Capitulo) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_CAPITULOS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.manga)
            st.setInt(2, obj.volume)
            st.setDouble(3, obj.capitulo)
            st.setString(4, obj.linguagem!!.sigla)
            st.setBoolean(5, obj.isExtra)
            st.setString(6, obj.scan)
            st.setBoolean(7, obj.isProcessado)
            st.setString(8, obj.getId()!!.toString())
            insertVocabulario(base, null, obj.getId(), null, obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updatePagina(base: String, obj: Pagina) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_PAGINAS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.nomePagina)
            st.setInt(2, obj.numero)
            st.setString(3, obj.hashPagina)
            st.setBoolean(4, obj.isProcessado)
            st.setString(5, obj.getId()!!.toString())
            insertVocabulario(base, null, null, obj.getId(), obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateTexto(base: String, obj: Texto) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            st.setInt(1, obj.sequencia)
            st.setString(2, obj.texto)
            st.setInt(3, obj.posicao_x1)
            st.setInt(4, obj.posicao_y1)
            st.setInt(5, obj.posicao_x2)
            st.setInt(6, obj.posicao_y2)
            st.setString(7, obj.getId()!!.toString())
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateVocabulario(base: String, obj: Vocabulario) {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun insertVolume(base: String, obj: Volume): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_VOLUMES, base),
                Statement.RETURN_GENERATED_KEYS
            )
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setString(++index, obj.linguagem!!.sigla)
            st.setString(++index, obj.arquivo)
            st.setBoolean(++index, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    insertVocabulario(base, id, null, null, obj.vocabulario)
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

    override fun insertCapitulo(base: String, idVolume: UUID, obj: Capitulo): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_CAPITULOS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idVolume.toString())
            st.setString(++index, obj.manga)
            st.setInt(++index, obj.volume)
            st.setFloat(++index, obj.capitulo.toFloat())
            st.setString(++index, obj.linguagem!!.sigla)
            st.setString(++index, obj.scan)
            st.setBoolean(++index, obj.isExtra)
            st.setBoolean(++index, obj.isRaw)
            st.setBoolean(++index, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    insertVocabulario(base, null, id, null, obj.vocabulario)
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

    override fun insertPagina(base: String, idCapitulo: UUID, obj: Pagina): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_PAGINAS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idCapitulo.toString())
            st.setString(++index, obj.nomePagina)
            st.setInt(++index, obj.numero)
            st.setString(++index, obj.hashPagina)
            st.setBoolean(++index, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: UUID? = null
                if (rs.next()) {
                    id = UUID.fromString(rs.getString(1))
                    insertVocabulario(base, null, null, id, obj.vocabulario)
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

    override fun insertTexto(base: String, idPagina: UUID, obj: Texto): UUID? {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(INSERT_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setString(++index, idPagina.toString())
            st.setInt(++index, obj.sequencia)
            st.setString(++index, obj.texto)
            st.setInt(++index, obj.posicao_x1)
            st.setInt(++index, obj.posicao_y1)
            st.setInt(++index, obj.posicao_x2)
            st.setInt(++index, obj.posicao_y2)
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

    override fun insertVocabulario(
        base: String,
        idVolume: UUID?,
        idCapitulo: UUID?,
        idPagina: UUID?,
        vocabulario: Set<Vocabulario>
    ) {
        var st: PreparedStatement? = null
        try {
            if (idVolume == null && idCapitulo == null && idPagina == null)
                return

            val campo = if (idVolume != null) "id_volume" else if (idCapitulo != null) "id_capitulo" else "id_pagina"
            val id = idVolume ?: (idCapitulo ?: idPagina)!!
            for (vocab in vocabulario) {
                st = conn.prepareStatement(
                    String.format(INSERT_VOCABULARIO, base, campo),
                    Statement.RETURN_GENERATED_KEYS
                )
                var index = 0
                st.setString(++index, id.toString())
                st.setString(++index, vocab.palavra)
                st.setString(++index, vocab.portugues)
                st.setString(++index, vocab.ingles)
                st.setString(++index, vocab.leitura)
                st.setBoolean(++index, vocab.isRevisado)
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

    // -------------------------------------------------------------------------------------------------------------  //

    override fun selectVolume(base: String, id: UUID): Volume? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUME, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getVolume(rs, base)
            else
                null
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectCapitulo(base: String, id: UUID): Capitulo? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPITULO, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getCapitulo(rs, base)
            else
                null
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectPagina(base: String, id: UUID): Pagina? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_PAGINA, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getPagina(rs, base)
            else
                null
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectTexto(base: String, id: UUID): Texto? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_TEXTO, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getTexto(rs)
            else
                null
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectVocabulario(base: String, id: UUID): Vocabulario? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOCABUALARIO, base, "id = ?"))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next()) {
                val id = UUID.fromString(rs.getString("id"))
                Vocabulario(
                    id,
                    rs.getString("palavra"),
                    rs.getString("portugues"),
                    rs.getString("ingles"),
                    rs.getString("leitura"),
                    rs.getBoolean("is_revisado"),
                    (if (rs.getObject("id_volume") != null) selectVolume(base, id) else null),
                    (if (rs.getObject("id_capitulo") != null) selectCapitulo(base, id) else null),
                    (if (rs.getObject("id_pagina") != null) selectPagina(base, id) else null)
                )
            } else
                null
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

    override fun selectAllVolumes(base: String): List<Volume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base))
            rs = st.executeQuery()
            val list: MutableList<Volume> = mutableListOf()
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

    override fun selectAllVolumes(base: String, pageable: Pageable): Page<Volume> {
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

            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + (if (pageable.sort.isEmpty) ""  else String.format(ORDER_BY, pageable.sort.toString())) + String.format(LIMIT, pageable.pageSize, pageable.pageNumber))
            rs = st.executeQuery()
            val list: MutableList<Volume> = mutableListOf()
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

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime): List<Volume> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + WHERE_DATE_SYNC)
            st.setTimestamp(1, Timestamp.valueOf(dateTime))
            rs = st.executeQuery()
            val list: MutableList<Volume> = mutableListOf()
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

    override fun selectAllVolumes(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<Volume> {
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

            st = conn.prepareStatement(String.format(SELECT_VOLUMES, base) + WHERE_DATE_SYNC + (if (pageable.sort.isEmpty) ""  else String.format(ORDER_BY, pageable.sort.toString())) + String.format(LIMIT, pageable.pageSize, pageable.pageNumber) )
            st.setTimestamp(1, time)
            rs = st.executeQuery()
            val list: MutableList<Volume> = mutableListOf()
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

    override fun selectAllCapitulos(base: String, idVolume: UUID): List<Capitulo> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_CAPITULOS, base))
            st.setString(1, idVolume.toString())
            rs = st.executeQuery()
            val list: MutableList<Capitulo> = mutableListOf()
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

    override fun selectAllPaginas(base: String, idCapitulo: UUID): List<Pagina> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_PAGINAS, base))
            st.setString(1, idCapitulo.toString())
            rs = st.executeQuery()
            val list: MutableList<Pagina> = mutableListOf()
            while (rs.next())
                list.add(getPagina(rs, base))
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

    override fun selectAllTextos(base: String, idPagina: UUID): List<Texto> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_TEXTOS, base))
            st.setString(1, idPagina.toString())
            rs = st.executeQuery()
            val list: MutableList<Texto> = mutableListOf()
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

    override fun selectAllVocabularios(
        base: String,
        idVolume: UUID?,
        idCapitulo: UUID?,
        idPagina: UUID?,
        comObj : Boolean
    ): Set<Vocabulario> {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun deleteVolume(base: String, obj: Volume) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_VOLUMES, base))
            st.setString(1, obj.getId().toString())
            conn.autoCommit = false
            conn.beginRequest()

            deleteVocabulario(base, obj.getId(), null, null)

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

    override fun deleteCapitulo(base: String, obj: Capitulo, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_CAPITULOS, base))
            st.setString(1, obj.getId().toString())

            if (transaction) {
                conn.autoCommit = false
                conn.beginRequest()
            }

            deleteVocabulario(base, null, obj.getId(), null)

            for (pagina in obj.paginas)
                deletePagina(base, pagina, false)

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

    override fun deletePagina(base: String, obj: Pagina, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE_PAGINAS, base))
            st.setString(1, obj.getId().toString())

            if (transaction) {
                conn.autoCommit = false
                conn.beginRequest()
            }

            deleteVocabulario(base, null, null, obj.getId())

            for (pagina in obj.textos)
                deleteTexto(base, pagina, false)

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

    override fun deleteTexto(base: String, obj: Texto, transaction : Boolean) {
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

    override fun deleteVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, transaction : Boolean) {
        var st: PreparedStatement? = null
        try {
            val campo = if (idVolume != null) "id_volume" else if (idCapitulo != null) "id_capitulo" else "id_pagina"
            val id = idVolume ?: (idCapitulo ?: idPagina)!!

            st = conn.prepareStatement(String.format(DELETE_VOCABULARIO, base, campo))

            st.setString(1, id.toString())
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

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    private fun createTriggers(nome: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_INSERT, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_TRIGGERS)
        } finally {
            closeStatement(st)
        }

        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_UPDATE, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_TRIGGERS)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }

        createTriggers(nome + TABELA_VOLUME)
        createTriggers(nome + TABELA_CAPITULO)
        createTriggers(nome + TABELA_PAGINA)
        createTriggers(nome + TABELA_TEXTO)
        createTriggers(nome + TABELA_VOCABULARIO)

    }

    override fun existTable(nome: String): Boolean {
        var st: PreparedStatement? = null
        val rs: ResultSet?
        try {
            st = conn.prepareStatement(
                String.format(
                    EXIST_TABELA_VOCABULARIO,
                    base,
                    nome
                )
            )
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
                st = conn.prepareStatement(
                    String.format(
                        SELECT_LISTA_TABELAS,
                        base
                    )
                )
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
}