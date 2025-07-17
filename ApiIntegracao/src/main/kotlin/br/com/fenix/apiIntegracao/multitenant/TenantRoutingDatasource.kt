package br.com.fenix.apiIntegracao.multitenant

import br.com.fenix.apiIntegracao.enums.Tenants
import br.com.fenix.apiIntegracao.exceptions.TableNotExistsException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource


@Component
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "multiEntityManager",
    transactionManagerRef = "multiTransactionManager",
    basePackages = ["br.com.fenix.apiIntegracao.repository"]
)
@EntityScan("br.com.fenix.apiIntegracao.model")
@ComponentScan(basePackages = ["br.com.fenix.apiIntegracao"])
class TenantRoutingDatasource : AbstractRoutingDataSource() {

    private val oLog = LoggerFactory.getLogger(TenantRoutingDatasource::class.java.name)

    @Autowired
    lateinit var tenantIdentifierResolver: TenantIdentifierResolver

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var decksubtitle: DataSource

    @Autowired
    lateinit var mangaextractor: DataSource

    @Autowired
    lateinit var textoingles: DataSource

    @Autowired
    lateinit var textojapones: DataSource

    private val PACKAGE_SCAN = "br.com.fenix.apiIntegracao.model"

    override fun determineCurrentLookupKey(): Any? = tenantIdentifierResolver.resolveCurrentTenantIdentifier()

    final var mConnections = hashMapOf<Any, Any>()

    fun getConnection(tenant: Tenants): DataSource {
        return when (tenant) {
            Tenants.MANGA_EXTRACTOR,
            Tenants.DECKSUBTITLE,
            Tenants.TEXTO_INGLES,
            Tenants.TEXTO_JAPONES -> mConnections[tenant]!! as DataSource
            else -> {
                oLog.warn("Não encontrado a base $tenant no servidor.")
                throw TableNotExistsException("Não encontrado a base $tenant no servidor.")
            }
        }
    }

    @Bean(name = ["multiRoutingDataSource"])
    fun multiRoutingDataSource(): DataSource {
        setDefaultTargetDataSource(dataSource)

        mConnections[Tenants.DEFAULT] = dataSource
        mConnections[Tenants.TEXTO_INGLES] = textoingles
        mConnections[Tenants.TEXTO_JAPONES] = textojapones

        mConnections[Tenants.DECKSUBTITLE] = decksubtitle
        mConnections[Tenants.MANGA_EXTRACTOR] = mangaextractor

        setTargetDataSources(mConnections)
        return this
    }

    @Bean(name = ["multiEntityManager"])
    fun multiEntityManager(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = multiRoutingDataSource()
        em.setPackagesToScan(PACKAGE_SCAN)
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.setJpaProperties(hibernateProperties())
        return em
    }

    @Bean(name = ["multiTransactionManager"])
    fun multiTransactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = multiEntityManager().getObject()
        return transactionManager
    }

    @Primary
    @Bean(name = ["entityManagerFactory"])
    fun dbSessionFactory(): LocalSessionFactoryBean {
        val sessionFactoryBean = LocalSessionFactoryBean()
        sessionFactoryBean.setDataSource(multiRoutingDataSource())
        sessionFactoryBean.setPackagesToScan(PACKAGE_SCAN)
        sessionFactoryBean.hibernateProperties = hibernateProperties()
        return sessionFactoryBean
    }

    private fun hibernateProperties(): Properties {
        val properties = Properties()
        properties["hibernate.show_sql"] = true
        properties["hibernate.format_sql"] = true
        properties["hibernate.dialect"] = "org.hibernate.dialect.MySQL8Dialect"
        properties["hibernate.id.new_generator_mappings"] = false
        properties["hibernate.jdbc.lob.non_contextual_creation"] = true
        return properties
    }

}