package br.com.fenix.apiIntegracao.multitenant

import org.hibernate.cfg.AvailableSettings
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Component
class NoOpConnectionProvider(@Autowired var datasource: DataSource) : MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    override fun isUnwrappableAs(p0: Class<*>?): Boolean = false

    override fun <T : Any?> unwrap(p0: Class<T>?): T {
        throw UnsupportedOperationException("Can't unwrap this.");
    }

    override fun getAnyConnection(): Connection = datasource.connection

    override fun releaseAnyConnection(conn: Connection?) {
        conn?.close()
    }

    override fun getConnection(p0: String?): Connection = datasource.connection

    override fun releaseConnection(p0: String?, conn: Connection?) {
        conn?.close()
    }

    override fun supportsAggressiveRelease(): Boolean = false

    override fun customize(hibernateProperties: MutableMap<String, Any>?) {
        hibernateProperties?.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}