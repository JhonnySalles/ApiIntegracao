package br.com.fenix.apiintegracao.component

import br.com.fenix.apiintegracao.config.FlywayConfig
import br.com.fenix.apiintegracao.enums.Driver
import br.com.fenix.apiintegracao.enums.Mapeamento
import br.com.fenix.apiintegracao.model.api.DadosConexao
import br.com.fenix.apiintegracao.repository.api.DadosConexaoRepository
import br.com.fenix.apiintegracao.scanner.RepositoryInterfaceScanner
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.DependsOn
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import javax.sql.DataSource

@Component
@DependsOn("flywayApi")
class DynamicJdbcRunner(private val repository: DadosConexaoRepository, private val registry: DynamicJdbcRegistry) : CommandLineRunner {

    companion object {
        private val oLog = LoggerFactory.getLogger(DynamicJdbcRunner::class.java.name)
    }

    override fun run(vararg args: String?) {
        oLog.info("Iniciando as conexões JDBC...")

        val conexoes = mutableListOf<DadosConexao>()
        conexoes.addAll(repository.findByAtivoIsTrueAndMapeamentoEquals(Mapeamento.JDBC))
        conexoes.addAll(repository.findByAtivoIsTrueAndMapeamentoEquals(Mapeamento.AMBOS))

        if (conexoes.isEmpty()) {
            oLog.info("Nenhuma conexão JDBC encontrada.")
            return
        }

        conexoes.forEach { config ->
            oLog.info("Executando migração para a conexão: [${config.base}]")
            try {
                val migracoes = FlywayConfig.flyway(config).migrate().migrationsExecuted
                oLog.info("Migração para [${config.base}] concluída. $migracoes migrações aplicadas.")
                registry.register(config.tipo, config, buildFactory(config))
            } catch (e: Exception) {
                oLog.error("Falha ao executar migração para a conexão [${config.base}]. Erro: ${e.message}", e)
            }
        }
    }

    private fun buildFactory(dados: DadosConexao): Connection {
        val properties = Properties()
        properties["user"] = dados.usuario
        properties["password"] = dados.senha
        properties["characterEncoding"] = "UTF-8"
        properties["useUnicode"] = "true"

        val url = dados.url + "/" + dados.base + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
        return DriverManager.getConnection(url, properties)
    }

}