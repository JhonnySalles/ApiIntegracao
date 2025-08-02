package br.com.fenix.apiintegracao.database

import br.com.fenix.apiintegracao.database.dao.ComicInfoDao
import br.com.fenix.apiintegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.database.dao.implement.ComicInfoDaoJDBC
import br.com.fenix.apiintegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiintegracao.database.dao.implement.MangaExtractorDaoJDBC
import br.com.fenix.apiintegracao.model.api.DadosConexao
import org.slf4j.LoggerFactory
import java.sql.Connection

object DaoFactory {
    private val oLog = LoggerFactory.getLogger(DaoFactory::class.java.name)

    fun createDeckSubtitleDao(source: Pair<DadosConexao, Connection>): DeckSubtitleDao {
        try {
            return DeckSubtitleDaoJDBC(source.second, source.first.base)
        } catch (e: Exception) {
            oLog.error("Error connect deck subtitle database", e)
            throw e
        }
    }

    fun createMangaExtractorDao(source: Pair<DadosConexao, Connection>): MangaExtractorDao {
        try {
            return MangaExtractorDaoJDBC(source.second, source.first.base)
        } catch (e: Exception) {
            oLog.error("Error connect manga extractor database", e)
            throw e
        }
    }

    fun createNovelExtractorDao(source: Pair<DadosConexao, Connection>): MangaExtractorDao {
        try {
            return MangaExtractorDaoJDBC(source.second, source.first.base)
        } catch (e: Exception) {
            oLog.error("Error connect manga extractor database", e)
            throw e
        }
    }

    fun createComicInfoDao(connection: Connection): ComicInfoDao {
        try {
            return ComicInfoDaoJDBC(connection)
        } catch (e: Exception) {
            oLog.error("Error connect comic info database", e)
            throw e
        }
    }

}