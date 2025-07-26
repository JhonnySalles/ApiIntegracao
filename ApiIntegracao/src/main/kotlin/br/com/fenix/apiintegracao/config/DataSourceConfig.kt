package br.com.fenix.apiintegracao.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean("apiDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

}