package br.com.fenix.apiIntegracao.multitenant

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class FlywaySlaveInitializer {

    @Autowired
    lateinit var dataSource: DataSource

    @PostConstruct
    fun migrateFlyway() {
        val flywayIntegration = Flyway.configure()
            .dataSource(dataSource)
            .locations("filesystem:./src/main/resources/db.migration")
            .load()
        flywayIntegration.migrate()
    }
}