package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.database.dao.ExtractorDaoBase
import br.com.fenix.apiintegracao.exceptions.EndpointUnavailableException
import br.com.fenix.apiintegracao.exceptions.RequiredParameterstIsNullException
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

abstract class ExtractorRepositoryBase<E : EntityBase<ID, E>, ID>(private val factory: EntityFactory<ID, E>) : RepositoryJdbcTabela<E, ID> {

    abstract val dao : ExtractorDaoBase<E, ID>
    abstract fun getTabela(obj: E) : String

    override fun update(obj: E): E {
        val tabela = getTabela(obj)
        if (!dao.existTable(tabela)) {
            dao.createTable(tabela)
            insert(tabela, obj)
        } else
            update(tabela, obj)
        return obj
    }

    override fun update(tabela: String, obj: E): E = dao.updateVolume(tabela, obj)

    override fun insert(obj: E): E {
        val tabela = getTabela(obj)
        if (!dao.existTable(tabela))
            dao.createTable(tabela)
        insert(tabela, obj)
        return obj
    }

    override fun insert(tabela: String, obj: E): E {
        obj.setId(dao.insertVolume(tabela, obj))
        return obj
    }

    override fun select(id: ID): Optional<E> = throw EndpointUnavailableException()

    override fun select(tabela: String, id: ID): Optional<E> = dao.selectVolume(tabela, id ?: throw RequiredParameterstIsNullException())

    override fun findAll(): List<E> = throw EndpointUnavailableException()

    override fun findAll(pageable: Pageable?): Page<E> = throw EndpointUnavailableException()

    override fun findAll(tabela: String): List<E> = dao.selectAllVolumes(tabela)

    override fun findAll(tabela: String, pageable: Pageable?): Page<E> = dao.selectAllVolumes(tabela, pageable ?: throw RequiredParameterstIsNullException())

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime): List<E> = throw EndpointUnavailableException()

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime, pageable: Pageable?): Page<E> = throw EndpointUnavailableException()

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<E> = dao.selectAllVolumes(tabela, dateTime)

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<E> = dao.selectAllVolumes(tabela, dateTime, pageable ?: throw RequiredParameterstIsNullException())

    override fun createtabela(tabela: String) = dao.createTable(tabela)

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

    override fun tabelas(): List<String> = dao.tables

    override fun delete(id: ID) = throw EndpointUnavailableException()

    override fun delete(obj: E) {
        val tabela = getTabela(obj)
        if (dao.existTable(tabela))
            delete(tabela, obj)
        else
            throw NotFoundException()
    }

    override fun delete(tabela: String, id: ID) = dao.deleteVolume(tabela, factory.create(id ?: throw RequiredParameterstIsNullException()))

    override fun delete(tabela: String, obj: E) = dao.deleteVolume(tabela, obj)

}