package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.database.dao.RepositoryDaoBase
import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.utils.Utils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

abstract class RepositoryJdbcBase<E : EntityBase<ID, E>, ID> : RepositoryJdbc<E, ID> {

    abstract val dao: RepositoryDaoBase<ID, E>

    @Throws(ExceptionDb::class)
    override fun update(obj: E): E {
        dao.update(obj, isThrowsNotUpdate = true)
        return obj
    }

    @Throws(ExceptionDb::class)
    override fun insert(obj: E): E {
        val id = dao.insert(obj, isThrowsNotInsert = true) ?: throw ExceptionDb("Failed to insert object, ID is null")
        obj.setId(id)
        return obj
    }

    @Throws(ExceptionDb::class)
    override fun select(id: ID): Optional<E> {
        if (id == null)
            return Optional.empty()
        return dao.find(id)
    }

    @Throws(ExceptionDb::class)
    override fun findAll(): List<E> {
        return dao.findAll()
    }

    @Throws(ExceptionDb::class)
    override fun findAll(pageable: Pageable?): Page<E> {
        val pageable = pageable ?: Utils.defaultPageable()
        return dao.findAll(pageable)
    }

    @Throws(ExceptionDb::class)
    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime): List<E> {
        return dao.findAll(mapOf(Pair("atualizacao", dateTime)))
    }

    @Throws(ExceptionDb::class)
    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime, pageable: Pageable?): Page<E> {
        val pageable = pageable ?: Utils.defaultPageable()
        return dao.findAll(mapOf(Pair("atualizacao", dateTime)), pageable)
    }

    @Throws(ExceptionDb::class)
    override fun delete(obj: E) {
        val id = obj.getId() ?: throw ExceptionDb("ID cannot be null for deletion")
        delete(id)
    }

    @Throws(ExceptionDb::class)
    override fun delete(id: ID) {
        if (id == null)
            throw ExceptionDb("ID cannot be null for deletion")
        dao.delete(id)
    }
}