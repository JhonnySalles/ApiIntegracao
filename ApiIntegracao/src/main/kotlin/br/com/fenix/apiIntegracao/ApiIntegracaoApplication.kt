package br.com.fenix.apiIntegracao

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class ApiIntegracaoApplication

fun main(args: Array<String>) {
    runApplication<ApiIntegracaoApplication>(*args)
}
