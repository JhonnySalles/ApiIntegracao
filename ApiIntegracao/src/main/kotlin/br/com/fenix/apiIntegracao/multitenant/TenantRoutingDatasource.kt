package br.com.fenix.apiIntegracao.multitenant

import br.com.fenix.apiIntegracao.enums.Tenants
import br.com.fenix.apiIntegracao.exceptions.TableNotExistsException
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import java.util.logging.Logger
import javax.sql.DataSource

@Component
@EnableAutoConfiguration
@EntityScan("br.com.fenix.apiIntegracao.model")
class TenantRoutingDatasource(@Autowired val tenantIdentifierResolver: TenantIdentifierResolver) : AbstractRoutingDataSource() {

    companion object {
        val LOG = Logger.getLogger(TenantRoutingDatasource::class.java.name)
    }

    @Primary
    @Bean(name = ["dataSource"])
    @ConfigurationProperties("spring.datasource")
    fun defaultConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["decksubtitle"])
    @ConfigurationProperties("servers.decksubtitle")
    fun deckSubtitleConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["mangaextractor"])
    @ConfigurationProperties("servers.mangaextractor")
    fun mangaExtractorConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["textoingles"])
    @ConfigurationProperties("servers.textoingles")
    fun textoInglesConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(name = ["textojapones"])
    @ConfigurationProperties("servers.textojapones")
    fun textoJaponesConnection(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    final var mConnections = hashMapOf<Tenants, DataSource>()

    init {
        val default = defaultConnection()
        setDefaultTargetDataSource(default)

        val targetDataSources = HashMap<Any, Any>()
        targetDataSources[Tenants.DEFAULT.name] = default
        val ingles = textoInglesConnection()
        targetDataSources[Tenants.TEXTO_INGLES.name] = ingles
        mConnections[Tenants.TEXTO_INGLES] = ingles
        val japones = textoJaponesConnection()
        targetDataSources[Tenants.TEXTO_JAPONES.name] = japones
        mConnections[Tenants.TEXTO_JAPONES] = japones
        setTargetDataSources(targetDataSources)

        mConnections[Tenants.DECKSUBTITLE] = deckSubtitleConnection()
        mConnections[Tenants.MANGA_EXTRACTOR] = mangaExtractorConnection()
    }

    override fun determineCurrentLookupKey(): Any? = tenantIdentifierResolver.resolveCurrentTenantIdentifier()

    fun getConnection(tenant: Tenants): DataSource {
        return when (tenant) {
            Tenants.MANGA_EXTRACTOR,
            Tenants.DECKSUBTITLE,
            Tenants.TEXTO_INGLES,
            Tenants.TEXTO_JAPONES -> mConnections[tenant]!!
            else -> {
                LOG.warning("Não encontrado a base $tenant no servidor.")
                throw TableNotExistsException("Não encontrado a base $tenant no servidor.")
            }
        }
    }
}