package br.com.fenix.apiIntegracao.database.mysql

import br.com.fenix.apiIntegracao.exceptions.TableNotConnectedException
import java.sql.*
import java.util.*

object DB {

    val PROP_URL = "url"
    val PROP_PORTA = "porta"
    val PROP_BASE = "base"
    val PROP_SERVER = "server"
    val PROP_PORT = "port"
    val PROP_USER = "user"
    val PROP_PASSWORD = "password"

    fun getConnection(prop: Properties): Connection {
        var conn: Connection? = null
        try {
            prop.setProperty("characterEncoding", "UTF-8")
            prop.setProperty("useUnicode", "true")
            val url = ("jdbc:mysql://" + prop.getProperty(PROP_URL) + ":" + prop.getProperty(PROP_PORTA) + "/" + prop.getProperty(PROP_BASE)
                    + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
            conn = DriverManager.getConnection(url, prop)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        if (conn == null)
            throw TableNotConnectedException("Não foi possível conectar ao banco de dados.")

        return conn
    }

    fun closeConnection(conn: Connection?) {
        if (conn != null) {
            try {
                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun closeStatement(st: Statement?) {
        if (st != null) {
            try {
                st.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun closeResultSet(rs: ResultSet?) {
        if (rs != null) {
            try {
                rs.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}