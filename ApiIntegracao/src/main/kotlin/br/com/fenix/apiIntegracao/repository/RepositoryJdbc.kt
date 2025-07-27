package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface RepositoryJdbc<E : EntityBase<ID, E>, ID> {
    @Throws(ExceptionDb::class)
    fun update(obj: E): E

    @Throws(ExceptionDb::class)
    fun insert(obj: E): E

    @Throws(ExceptionDb::class)
    fun select(id: ID): Optional<E>

    @Throws(ExceptionDb::class)
    fun findAll(): List<E>

    @Throws(ExceptionDb::class)
    fun findAll(pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime): List<E>

    @Throws(ExceptionDb::class)
    fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime, pageable: Pageable?): Page<E>

    @Throws(ExceptionDb::class)
    fun delete(obj: E)

    @Throws(ExceptionDb::class)
    fun delete(id: ID)
}