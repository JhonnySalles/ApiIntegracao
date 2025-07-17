package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiIntegracao.database.dao.implement.MangaExtractorDaoJDBC
import org.slf4j.LoggerFactory
import javax.sql.DataSource


object DaoFactory {
    private val oLog = LoggerFactory.getLogger(DaoFactory::class.java.name)

    fun createDeckSubtitleDao(datasource: DataSource): DeckSubtitleDao {
        try {
            val conn = datasource.connection
            return DeckSubtitleDaoJDBC(conn, conn.schema)
        } catch (e: Exception) {
            oLog.error("Error connect deck subtitle database", e)
            throw e
        }
    }

    fun createMangaExtractorDao(datasource: DataSource): MangaExtractorDao {
        try {
            val conn = datasource.connection
            return MangaExtractorDaoJDBC(conn, conn.schema)
        } catch (e: Exception) {
            oLog.error("Error connect manga extractor database", e)
            throw e
        }
    }
}