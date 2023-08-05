package br.com.fenix.apiIntegracao.database.mysql

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import java.sql.*
import java.util.*

object DB {
    fun getConnection(prop: Properties, base: String): Connection {
        var conn: Connection? = null
        try {
            prop.setProperty("characterEncoding", "UTF-8")
            prop.setProperty("useUnicode", "true")
            val url = ("jdbc:mysql://" + prop.getProperty("server") + ":" + prop.getProperty("port") + "/"
                    + base
                    + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
            conn = DriverManager.getConnection(url, prop)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        if (conn == null)
            throw ExceptionDb("Não foi possível conectar ao banco de dados.")

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