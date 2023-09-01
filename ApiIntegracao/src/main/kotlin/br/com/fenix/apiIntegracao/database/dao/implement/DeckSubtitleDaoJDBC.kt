package br.com.fenix.apiIntegracao.database.dao.implement

import br.com.fenix.apiIntegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiIntegracao.database.mysql.DB.closeResultSet
import br.com.fenix.apiIntegracao.database.mysql.DB.closeStatement
import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.messages.Mensagens
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.model.mangaextractor.*
import java.sql.*
import java.util.*

class DeckSubtitleDaoJDBC(private val conn: Connection, private val base: String) : DeckSubtitleDao {

    companion object {
        private const val UPDATE = "UPDATE %s SET Episodio = ?, Linguagem = ?, TempoInicial = ?, TempoFinal = ?, Texto = ?, Traducao = ?, Vocabulario = ? WHERE id = ?"

        private const val INSERT = "INSERT INTO %s (id, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario) VALUES (?,?,?,?,?,?,?,?)"

        private const val DELETE = "DELETE %s WHERE id = ?"

        private const val SELECT = "SELECT id, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario  FROM %s WHERE id = ?"

        private const val SELECT_ALL = "SELECT id, Episodio, Linguagem, TempoInicial, TempoFinal, Texto, Traducao, Vocabulario  FROM %s"

        private const val CREATE_TABELA =
            ("CREATE TABLE %s (id varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL, Episodio int(2) DEFAULT NULL, "
                    + "  Linguagem varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL, TempoInicial varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL, "
                    + "  TempoFinal varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL, Texto longtext COLLATE utf8mb4_unicode_ci, "
                    + "  Traducao longtext COLLATE utf8mb4_unicode_ci, Vocabulario longtext COLLATE utf8mb4_unicode_ci, "
                    + "  Atualizacao` datetime DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci")

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
                "    SET new.Atualizacao = NOW();" +
                "  END$$" +
                "DELIMITER ;"

        private const val EXIST_TABELA = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " AND Table_Name LIKE '%%%s%%' GROUP BY Tabela ")

        private const val SELECT_LISTA_TABELAS = ("SELECT REPLACE(Table_Name, '_volumes', '') AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = '%s' "
                + " GROUP BY Tabela ")
    }

    // -------------------------------------------------------------------------------------------------------------  //
    private fun getLegenda(rs: ResultSet, base: String): Legenda {
        return Legenda(
            UUID.fromString(rs.getString("id")),
            rs.getInt("episodio"),
            rs.getString("linguagem"),
            rs.getString("tempoInicial"),
            rs.getString("tempoFinal"),
            rs.getString("texto"),
            rs.getString("traducao"),
            (if (rs.getObject("vocabulario") != null) rs.getString("vocabulario") else null)
        )
    }

    override fun update(base: String, obj: Legenda) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE, base),
                Statement.RETURN_GENERATED_KEYS
            )
            var index = 0
            st.setInt(++index, obj.episodio)
            st.setString(++index, obj.linguagem)
            st.setString(++index, obj.tempoInicial)
            st.setString(++index, obj.tempoFinal)
            st.setString(++index, obj.texto)
            st.setString(++index, obj.traducao)
            st.setString(++index, obj.vocabulario)
            st.setString(++index, obj.getId()!!.toString())

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

    override fun insert(base: String, obj: Legenda): UUID? {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT, base),
                Statement.RETURN_GENERATED_KEYS
            )
            val id = if (obj.getId() != null) obj.getId() else UUID.randomUUID()
            var index = 0
            st.setString(++index, id.toString())
            st.setInt(++index, obj.episodio)
            st.setString(++index, obj.linguagem)
            st.setString(++index, obj.tempoInicial)
            st.setString(++index, obj.tempoFinal)
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
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun select(base: String, id: UUID): Legenda? {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT, base))
            st.setString(1, id.toString())
            rs = st.executeQuery()
            if (rs.next())
                getLegenda(rs, base)
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

    override fun selectAll(base: String): List<Legenda> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(String.format(SELECT_ALL, base))
            rs = st.executeQuery()
            val list: MutableList<Legenda> = mutableListOf()
            while (rs.next())
                list.add(getLegenda(rs, base))
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

    override fun delete(base: String, obj: Legenda) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(DELETE, base))
            st.setString(1, obj.getId().toString())
            conn.autoCommit = false
            conn.beginRequest()
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

    // -------------------------------------------------------------------------------------------------------------  //

    override fun existTable(nome: String): Boolean {
        var st: PreparedStatement? = null
        val rs: ResultSet?
        try {
            st = conn.prepareStatement(
                String.format(
                    EXIST_TABELA,
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

    override fun createTable(nome: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(
                    CREATE_TABELA, nome
                )
            )
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }

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

    override fun selectAllTabelas(): List<Tabela> {
        TODO("Not yet implemented")
    }

    @get:Throws(ExceptionDb::class)
    override val tables: List<String>
        get() {
            var st: PreparedStatement? = null
            var rs: ResultSet? = null
            return try {
                st = conn.prepareStatement(
                    String.format(
                        SELECT_LISTA_TABELAS, base
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