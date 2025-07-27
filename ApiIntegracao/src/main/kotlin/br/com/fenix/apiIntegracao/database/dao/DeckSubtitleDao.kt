package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
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
    fun selectAll(base: String, pageable: Pageable): Page<Legenda>

    @Throws(ExceptionDb::class)
    fun selectAll(base: String, dateTime: LocalDateTime): List<Legenda>

    @Throws(ExceptionDb::class)
    fun selectAll(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<Legenda>

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