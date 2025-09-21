package br.com.fenix.apiintegracao.database.dao.implement

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeResultSet
import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry.Companion.closeStatement
import br.com.fenix.apiintegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiintegracao.database.dao.implement.ComicInfoDaoJDBC.Companion
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.messages.Mensagens
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import java.sql.*
import java.time.LocalDateTime
import java.util.*

class DeckSubtitleDaoJDBC(private val conn: Connection, private val base: String) : DeckSubtitleDao {

    companion object {
        private val oLog = LoggerFactory.getLogger(DeckSubtitleDaoJDBC::class.java)

        private const val INSERT = "INSERT INTO %s (ID, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
        private const val UPDATE = "UPDATE %s SET Episodio = ?, Linguagem = ?, TempoInicial = ?, TempoFinal = ?, Texto = ?, Traducao = ?, Vocabulario = ? WHERE id = ?;"
        private const val SELECT = "SELECT id, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario FROM %s WHERE id = ?;"
        private const val DELETE = "DELETE %s WHERE id = ?"

        private const val SELECT_ALL = "SELECT id, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario  FROM %s"

        private const val SELECT_COUNT = "SELECT count(*) as total FROM %s"

        private const val WHERE_DATE_SYNC = " WHERE atualizacao >= ?"
        private const val ORDER_BY = " ORDER BY %s"
        private const val LIMIT = " LIMIT %s OFFSET %s"

        private const val CREATE_TABELA = "CALL create_table('%s');"
        private const val CREATE_TRIGGER_INSERT = "CREATE TRIGGER %s_insert BEFORE INSERT ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    IF (NEW.id IS NULL OR NEW.id = '') THEN" +
                "      SET new.id = UUID();" +
                "    END IF;" +
                "  END"
        private const val CREATE_TRIGGER_UPDATE = "CREATE TRIGGER %s_update BEFORE UPDATE ON %s" +
                "  FOR EACH ROW BEGIN" +
                "    SET new.Atualizacao = NOW();" +
                "  END"

        private const val EXIST_TABELA = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%%s%%' GROUP BY Tabela ")

        private const val SELECT_LISTA_TABELAS = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " GROUP BY Tabela ")
    }


    // -------------------------------------------------------------------------------------------------------------  //

    private fun <E> toPageable(pageable: Pageable, total: Int, list: List<E>): Page<E> {
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
    private fun getLegenda(rs: ResultSet): Legenda {
        return Legenda(
            UUID.fromString(rs.getString("id")), 0, rs.getInt("episodio"),
            Linguagens.getEnum(rs.getString("linguagem").lowercase(Locale.getDefault()))!!,
            rs.getString("tempoinicial"), rs.getString("texto"), rs.getString("traducao"),
            rs.getString("vocabulario")
        )
    }

    override fun update(tabela: String, obj: Legenda) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE, tabela), Statement.RETURN_GENERATED_KEYS)
            var index = 0
            st.setInt(++index, obj.episodio)
            st.setString(++index, obj.linguagem.sigla.uppercase(Locale.getDefault()))
            st.setString(++index, obj.tempo)
            st.setNString(++index, null)
            st.setString(++index, obj.texto)
            st.setString(++index, obj.traducao)
            st.setString(++index, obj.vocabulario)
            st.setString(++index, obj.getId().toString())

            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun insert(tabela: String, obj: Legenda): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(String.format(INSERT, tabela), Statement.RETURN_GENERATED_KEYS)
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setInt(++index, obj.episodio)
            st.setString(++index, obj.linguagem.sigla.uppercase(Locale.getDefault()))
            st.setString(++index, obj.tempo)
            st.setNString(++index, null)
            st.setString(++index, obj.texto)
            st.setString(++index, obj.traducao)
            st.setString(++index, obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                if (rs.next())
                    return UUID.fromString(rs.getString(1))
                return null
            }
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun select(tabela: String, id: UUID): Legenda? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT, tabela))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getLegenda(rs)
            else
                null
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAll(tabela: String): List<Legenda> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_ALL, tabela))
            rs = st.executeQuery()
            val list: MutableList<Legenda> = mutableListOf()
            while (rs.next())
                list.add(getLegenda(rs))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAll(tabela: String, pageable: Pageable): Page<Legenda> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_COUNT, tabela))
            rs = st.executeQuery()
            var total = 1
            if (rs.next())
                total = rs.getInt(1)
            if (total < 1)
                total = 1

            st = conn.prepareStatement(
                String.format(SELECT_ALL, tabela) +
                        (if (pageable.sort.isEmpty) "" else String.format(ORDER_BY, pageable.sort).replace(":", "")) +
                        String.format(LIMIT, pageable.pageSize, pageable.pageNumber)
            )
            rs = st.executeQuery()
            val list: MutableList<Legenda> = mutableListOf()
            while (rs.next())
                list.add(getLegenda(rs))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAll(tabela: String, dateTime: LocalDateTime): List<Legenda> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_ALL, tabela) + WHERE_DATE_SYNC)
            st.setTimestamp(1, Timestamp.valueOf(dateTime))
            rs = st.executeQuery()
            val list: MutableList<Legenda> = mutableListOf()
            while (rs.next())
                list.add(getLegenda(rs))
            list
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun selectAll(tabela: String, dateTime: LocalDateTime, pageable: Pageable): Page<Legenda> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            val time = Timestamp.valueOf(dateTime)
            st = conn.prepareStatement(String.format(SELECT_COUNT, tabela) + WHERE_DATE_SYNC)
            st.setTimestamp(1, time)
            rs = st.executeQuery()
            var total = 1
            if (rs.next())
                total = rs.getInt(1)
            if (total < 1)
                total = 1

            st = conn.prepareStatement(
                String.format(SELECT_ALL, tabela) + WHERE_DATE_SYNC + (if (pageable.sort.isEmpty) "" else String.format(ORDER_BY, pageable.sort.toString())) + String.format(
                    LIMIT, pageable.pageSize, pageable.pageNumber
                )
            )
            st.setTimestamp(1, time)
            rs = st.executeQuery()
            val list: MutableList<Legenda> = mutableListOf()
            while (rs.next())
                list.add(getLegenda(rs))

            toPageable(pageable, total, list)
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
        } finally {
            closeStatement(st)
            closeResultSet(rs)
        }
    }

    override fun delete(tabela: String, obj: Legenda) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE, tabela))
            st.setString(1, obj.getId().toString())
            conn.autoCommit = false
            conn.beginRequest()
            st.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                oLog.error(e1.message, e1)
            }
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                oLog.error(e.message, e)
            }
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun existTable(nome: String): Boolean {
        var st: PreparedStatement? = null
        val rs: ResultSet?
        try {
            st = conn.prepareStatement(String.format(EXIST_TABELA, base, nome))
            rs = st.executeQuery()
            return rs.next()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    override fun createTable(base: String) {
        val nome: String = base.trim()
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(CREATE_TABELA, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_INSERT, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_TRIGGER_UPDATE, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            oLog.error("Error ao executar o comando: " + st.toString(), e)
            throw SQLException(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

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
}