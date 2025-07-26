package br.com.fenix.apiintegracao.containers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.lifecycle.Startables
import java.util.stream.Stream

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest {
    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        companion object {
            var mysql: MySQLContainer<*> = MySQLContainer("mysql:8.0.29")
            private fun startContainers() {
                Startables.deepStart(Stream.of(mysql)).join()
            }

            private fun createConnectionConfiguration(): Map<String, String> {
                return java.util.Map.of(
                    "spring.datasource.url", mysql.jdbcUrl,
                    "spring.datasource.username", mysql.username,
                    "spring.datasource.password", mysql.password
                )
            }
        }

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()
            val environment = applicationContext.environment
            val testcontainers = MapPropertySource(
                "testcontainers",
                createConnectionConfiguration()
            )
            environment.propertySources.addFirst(testcontainers)
        }
    }
}