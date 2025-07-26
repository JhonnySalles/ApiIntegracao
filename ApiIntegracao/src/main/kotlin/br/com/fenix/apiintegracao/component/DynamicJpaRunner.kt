package br.com.fenix.apiintegracao.component

import br.com.fenix.apiintegracao.config.FlywayConfig
import br.com.fenix.apiintegracao.enums.Driver
import br.com.fenix.apiintegracao.model.api.DadosConexao
import br.com.fenix.apiintegracao.repository.DynamicRepositoryRegistry
import br.com.fenix.apiintegracao.repository.api.DadosConexaoRepository
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.DependsOn
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import java.util.*
import javax.sql.DataSource

@Component
@DependsOn("flywayApi")
class DynamicJpaRunner(private val repository: DadosConexaoRepository, private val registry: DynamicRepositoryRegistry, private val emfBuilder: EntityManagerFactoryBuilder) : CommandLineRunner {

    companion object {
        private val oLog = LoggerFactory.getLogger(DynamicJpaRunner::class.java.name)
    }

    override fun run(vararg args: String?) {
        oLog.info("Iniciando as conexões...")

        val conexoes = repository.findByAtivoIsTrue()

        if (conexoes.isEmpty()) {
            oLog.info("Nenhuma conexão encontrada.")
            return
        }

        conexoes.forEach { config ->
            oLog.info("Executando migração para a conexão: [${config.base}]")
            try {
                val dataSource = createDataSourceFromConfig(config)

                val migracoes = FlywayConfig.flyway(config).migrate().migrationsExecuted
                oLog.info("Migração para [${config.base}] concluída. $migracoes migrações aplicadas.")

                val emf = createEntityManagerFactory(config, dataSource)
                val entityManager = emf.createEntityManager()
                val repositoryFactory = JpaRepositoryFactory(entityManager)
                registerRepositoriesInPackage(config, repositoryFactory, registry)
                registry.registerEntityManager(config.tipo, entityManager)
                oLog.info("Repositórios para [${config.base}] criados e registrados com sucesso.")

            } catch (e: Exception) {
                oLog.error("Falha ao executar migração para a conexão [${config.base}]. Erro: ${e.message}", e)
            }
        }
    }

    private fun createEntityManagerFactory(config: DadosConexao, dataSource: DataSource): EntityManagerFactory {
        val factoryBean = emfBuilder
            .dataSource(dataSource)
            .packages("br.com.fenix.apiIntegracao.model.${config.tipo.packages.lowercase(Locale.getDefault())}")
            .persistenceUnit(config.tipo.name)
            .build()

        factoryBean.afterPropertiesSet()
        return factoryBean.getObject()!!
    }

    private fun createDataSourceFromConfig(config: DadosConexao): DataSource {
        val hikariDataSource = HikariDataSource()
        when(config.driver) {
            Driver.MYSQL -> hikariDataSource.driverClassName = "com.mysql.cj.jdbc.Driver"
            Driver.POSTGRE -> hikariDataSource.driverClassName = "org.postgresql.Driver"
        }
        hikariDataSource.jdbcUrl = config.url
        hikariDataSource.username = config.usuario
        hikariDataSource.password = config.senha

        // --- Configurações Adicionais do Pool de Conexões ---
        hikariDataSource.poolName = "${config.tipo.name}-HikariPool"
        hikariDataSource.maximumPoolSize = 10
        hikariDataSource.minimumIdle = 2
        hikariDataSource.connectionTimeout = 30000
        hikariDataSource.idleTimeout = 600000

        return hikariDataSource
    }

    private fun registerRepositoriesInPackage(config: DadosConexao, repositoryFactory: JpaRepositoryFactory, registry: DynamicRepositoryRegistry) {
        val pasta = "br.com.fenix.apiintegracao.repository." + config.tipo.packages.lowercase(Locale.getDefault())
        oLog.info("Buscando repositórios no pacote: [$pasta]")

        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AssignableTypeFilter(Repository::class.java))

        val candidates = scanner.findCandidateComponents(pasta)
        if (candidates.isEmpty()) {
            oLog.warn("Nenhum repositório encontrado no pacote [$pasta]")
            return
        }

        for (candidate in candidates) {
            try {
                val repoClass = Class.forName(candidate.beanClassName)
                if (repoClass.isInterface) {
                    val repoInstance = repositoryFactory.getRepository(repoClass)
                    registry.register(config.tipo, repoClass, repoInstance)
                    oLog.info("  -> Repositório registrado dinamicamente: [${repoClass.simpleName}]")
                }
            } catch (e: ClassNotFoundException) {
                oLog.error("Erro ao carregar a classe do repositório: ${candidate.beanClassName}", e)
            }
        }
    }
}