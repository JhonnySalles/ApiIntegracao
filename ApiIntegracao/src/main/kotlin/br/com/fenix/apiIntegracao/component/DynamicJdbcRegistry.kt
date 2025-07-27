package br.com.fenix.apiintegracao.component

import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.api.DadosConexao
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.concurrent.ConcurrentHashMap

@Service
class DynamicJdbcRegistry {
    private val connections = ConcurrentHashMap<Conexao, Pair<DadosConexao, Connection>>()

    fun register(conexao: Conexao, dados: DadosConexao, connection: Connection) {
        connections[conexao] = Pair(dados, connection)
    }

    fun getConnection(conexao: Conexao): Connection {
        return connections[conexao]?.second ?: throw SQLException("Connection for ${conexao.name} not found.")
    }

    fun getDados(conexao: Conexao): DadosConexao {
        return connections[conexao]?.first ?: throw SQLException("Connection for ${conexao.name} not found.")
    }

    fun getSource(conexao: Conexao) : Pair<DadosConexao, Connection> {
        return connections[conexao] ?: throw SQLException("Connection for ${conexao.name} not found.")
    }

    companion object {
        private val oLog = LoggerFactory.getLogger(DynamicJdbcRegistry::class.java)

        fun closeStatement(st: Statement?) {
            if (st != null)
                try {
                    st.close()
                } catch (e: SQLException) {
                    oLog.error(e.message, e)
                }
        }

        fun closeResultSet(rs: ResultSet?) {
            if (rs != null)
                try {
                    rs.close()
                } catch (e: SQLException) {
                    oLog.error(e.message, e)
                }
        }
    }

}