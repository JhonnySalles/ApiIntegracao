package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.model.mangaextractor.*
import java.util.*

interface DeckSubtitleDao {

    @Throws(ExceptionDb::class)
    fun update(base: String, obj: Legenda)

    @Throws(ExceptionDb::class)
    fun insert(base: String, obj: Legenda): UUID?

    @Throws(ExceptionDb::class)
    fun select(base: String, id: UUID): Legenda?

    @Throws(ExceptionDb::class)
    fun selectAll(base: String): List<Legenda>

    @Throws(ExceptionDb::class)
    fun delete(base: String, obj: Legenda)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createTable(nome: String)

    @Throws(ExceptionDb::class)
    fun existTable(nome: String): Boolean

    @get:Throws(ExceptionDb::class)
    val tables: List<String>
}