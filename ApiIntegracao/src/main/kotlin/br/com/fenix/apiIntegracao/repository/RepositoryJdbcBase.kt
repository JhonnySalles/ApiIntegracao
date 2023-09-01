package br.com.fenix.apiIntegracao.repository

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.EntityBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface RepositoryJdbcBase<E : EntityBase<E, ID>, ID> {
    @Throws(ExceptionDb::class)
    fun update(table: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun insert(table: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun select(table: String, id: ID): Optional<E>

    @Throws(ExceptionDb::class)
    fun findAll(table: String): List<E>

    @Throws(ExceptionDb::class)
    fun findAll(table: String, pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime): List<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime, pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun delete(table: String, obj: E)

    @Throws(ExceptionDb::class)
    fun delete(table: String, id: ID)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createTable(table: String)

    @Throws(ExceptionDb::class)
    fun existsTable(table: String): Boolean

    @Throws(ExceptionDb::class)
    fun tables(): List<String>
}