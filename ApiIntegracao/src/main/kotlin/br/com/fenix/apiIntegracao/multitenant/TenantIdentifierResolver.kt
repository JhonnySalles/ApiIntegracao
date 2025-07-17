package br.com.fenix.apiIntegracao.multitenant

import br.com.fenix.apiIntegracao.enums.Tenants
import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component


@Component
class TenantIdentifierResolver : CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private var currentTenant = Tenants.UNKNOWN

    fun setDefaultTenant() {
        setCurrentTenant(Tenants.DEFAULT)
    }

    fun setCurrentTenant(tenant: Tenants) {
        currentTenant = tenant
    }

    override fun resolveCurrentTenantIdentifier(): String {
        return currentTenant.name
    }

    override fun validateExistingCurrentSessions(): Boolean = false

    override fun customize(hibernateProperties: MutableMap<String, Any>?) {
        hibernateProperties?.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}