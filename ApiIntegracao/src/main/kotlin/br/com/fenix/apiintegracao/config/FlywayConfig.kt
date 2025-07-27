package br.com.fenix.apiintegracao.config

import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.api.DadosConexao
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig {

    @Bean(initMethod = "migrate")
    fun flywayApi(@Qualifier("apiDataSource") primaryDataSource: DataSource): Flyway = flyway(Conexao.API, primaryDataSource)

    companion object {
        private val schema = "filesystem:src/main/resources/db/migration"
        private fun getSchema(tipo: Conexao): Array<String> {
            return when (tipo) {
                Conexao.API -> arrayOf("$schema/api")
                Conexao.MANGA_EXTRACTOR -> arrayOf("$schema/mangaextractor")
                Conexao.NOVEL_EXTRACTOR -> arrayOf("$schema/novelextractor")
                Conexao.DECKSUBTITLE -> arrayOf("$schema/decksubtitle")
                Conexao.TEXTO_INGLES -> arrayOf("$schema/textoingles")
                Conexao.TEXTO_JAPONES -> arrayOf("$schema/textojapones")
                Conexao.PROCESSA_TEXTO -> arrayOf("$schema/processatexto")
                else -> throw Exception("Não é possível realizar o migration, base não suportada ou não configurada.")
            }
        }

        fun flyway(tipo: Conexao, dataSource: DataSource): Flyway {
            return Flyway.configure()
                .locations(*getSchema(tipo))
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .load()
        }

        fun flyway(conexao: DadosConexao): Flyway {
            return Flyway.configure()
                .locations(*getSchema(conexao.tipo))
                .dataSource(conexao.url + "/" + conexao.base + "?createDatabaseIfNotExist=true", conexao.usuario, conexao.senha)
                .baselineOnMigrate(true)
                .load()
        }
    }

}