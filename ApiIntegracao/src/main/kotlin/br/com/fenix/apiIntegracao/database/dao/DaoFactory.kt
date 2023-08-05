package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.MangaDaoJDBC
import br.com.fenix.apiIntegracao.database.mysql.DB
import java.util.*

object DaoFactory {
    fun createMangaDao(prop: Properties, base: String): MangaExtractorDao {
        return MangaDaoJDBC(DB.getConnection(prop, base))
    }
}