package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiIntegracao.database.dao.implement.MangaExtractorDaoJDBC
import br.com.fenix.apiIntegracao.database.mysql.DB
import br.com.fenix.apiIntegracao.service.api.TabelasService.Companion.PROP_BASE
import java.util.*

object DaoFactory {
    fun createDeckSubtitleDao(prop: Properties): DeckSubtitleDao {
        return DeckSubtitleDaoJDBC(DB.getConnection(prop), prop.getProperty(PROP_BASE))
    }

    fun createMangaExtractorDao(prop: Properties): MangaExtractorDao {
        return MangaExtractorDaoJDBC(DB.getConnection(prop), prop.getProperty(PROP_BASE))
    }
}