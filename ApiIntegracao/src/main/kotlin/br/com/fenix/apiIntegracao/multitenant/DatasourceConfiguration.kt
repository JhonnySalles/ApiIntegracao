package br.com.fenix.apiIntegracao.multitenant

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
class DatasourceConfiguration {

    private val oLog = LoggerFactory.getLogger(DatasourceConfiguration::class.java.name)

    @Primary
    @Bean(name = ["dataSource"])
    @ConfigurationProperties("spring.datasource.api")
    fun defaultConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["decksubtitle"])
    @ConfigurationProperties("datasource.decksubtitle")
    fun deckSubtitleConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["mangaextractor"])
    @ConfigurationProperties("datasource.mangaextractor")
    fun mangaExtractorConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["textoingles"])
    @ConfigurationProperties("datasource.textoingles")
    fun textoInglesConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["textojapones"])
    @ConfigurationProperties("datasource.textojapones")
    fun textoJaponesConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

}