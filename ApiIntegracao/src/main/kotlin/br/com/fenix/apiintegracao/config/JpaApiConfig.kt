package br.com.fenix.apiintegracao.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["br.com.fenix.apiintegracao.repository.api"],
    entityManagerFactoryRef = "apiEntityManagerFactory",
    transactionManagerRef = "apiTransactionManager"
)
class JpaApiConfig {

    @Primary
    @Bean(name = ["apiEntityManagerFactory"])
    fun primaryEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("apiDataSource") dataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("br.com.fenix.apiintegracao.model.api")
            .persistenceUnit("api")
            .build()
    }

    @Primary
    @Bean(name = ["primaryTransactionManager"])
    fun primaryTransactionManager(
        @Qualifier("apiEntityManagerFactory") entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory.getObject()!!)
    }
}