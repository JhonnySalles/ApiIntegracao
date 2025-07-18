package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiIntegracao.database.dao.implement.MangaExtractorDaoJDBC
import br.com.fenix.apiIntegracao.exceptions.CustomizedResponseEntityExceptionHandler
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger
import javax.sql.DataSource

object DaoFactory {
    val LOG = Logger.getLogger(DaoFactory::class.java.name)

    fun createDeckSubtitleDao(datasource: DataSource): DeckSubtitleDao {
        try {
            val conn = datasource.connection
            return DeckSubtitleDaoJDBC(conn, conn.schema)
        } catch (e: Exception) {
            LOG.log(Level.SEVERE, "Error connect deck subtitle database", e)
            throw e
        }
    }

    fun createMangaExtractorDao(datasource: DataSource): MangaExtractorDao {
        try {
            val conn = datasource.connection
            return MangaExtractorDaoJDBC(conn, conn.schema)
        } catch (e: Exception) {
            LOG.log(Level.SEVERE, "Error connect manga extractor database", e)
            throw e
        }
    }
}