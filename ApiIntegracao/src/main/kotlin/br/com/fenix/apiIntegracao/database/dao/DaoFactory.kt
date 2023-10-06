package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.database.dao.implement.DeckSubtitleDaoJDBC
import br.com.fenix.apiIntegracao.database.dao.implement.MangaExtractorDaoJDBC
import java.sql.Connection

object DaoFactory {
    fun createDeckSubtitleDao(conn: Connection): DeckSubtitleDao {
        return DeckSubtitleDaoJDBC(conn, conn.schema)
    }

    fun createMangaExtractorDao(conn: Connection): MangaExtractorDao {
        return MangaExtractorDaoJDBC(conn, conn.schema)
    }
}