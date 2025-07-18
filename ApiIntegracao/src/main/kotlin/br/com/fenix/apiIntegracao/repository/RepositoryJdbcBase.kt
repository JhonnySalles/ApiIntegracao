package br.com.fenix.apiIntegracao.repository

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.EntityBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface RepositoryJdbcBase<E : EntityBase<E, ID>, ID> {
    @Throws(ExceptionDb::class)
    fun update(tabela: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun insert(tabela: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun select(tabela: String, id: ID): Optional<E>

    @Throws(ExceptionDb::class)
    fun findAll(tabela: String): List<E>

    @Throws(ExceptionDb::class)
    fun findAll(tabela: String, pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun delete(tabela: String, obj: E)

    @Throws(ExceptionDb::class)
    fun delete(tabela: String, id: ID)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createtabela(tabela: String)

    @Throws(ExceptionDb::class)
    fun existstabela(tabela: String): Boolean

    @Throws(ExceptionDb::class)
    fun tabelas(): List<String>
}