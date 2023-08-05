package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiIntegracao.database.dao.implement.MangaExtractorDaoJDBC
import br.com.fenix.apiIntegracao.database.mysql.DB
import java.util.*

object DaoFactory {
    fun createDeckSubtitleDao(prop: Properties, base: String): DeckSubtitleDao {
        return DeckSubtitleDaoJDBC(DB.getConnection(prop, base), base)
    }

    fun createMangaExtractorDao(prop: Properties, base: String): MangaExtractorDao {
        return MangaExtractorDaoJDBC(DB.getConnection(prop, base), base)
    }
}