package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJdbc
import br.com.fenix.apiintegracao.controller.ControllerJdbcBase
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.ServerErrorException
import br.com.fenix.apiintegracao.exceptions.TableNotExistsException
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.repository.RepositoryJdbc
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import br.com.fenix.apiintegracao.service.ServiceJpaBase.Companion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

abstract class ServiceJdbcBase<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbc<ID, E, D, C>>(
    open var repository: RepositoryJdbc<E, ID>, open var assembler: PagedResourcesAssembler<D>, open val clazzEntity: Class<E>, open val clazzDto: Class<D>, open val clazzController: Class<C>
) {

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(ServiceJdbcBase::class.java)
    }

    fun getPage(pageable: Pageable): PagedModel<EntityModel<D>> {
        try {
            val list = repository.findAll(pageable).map { addLink(toDto(it)) }
            val link = linkTo(methodOn(clazzController).getPage(list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            oLog.error("Error get page on jpa base", e)
            throw e
        }
    }

    open fun getPage(updateDate: String, pageable: Pageable): PagedModel<EntityModel<D>> {
        val dateTime = LocalDateTime.parse(updateDate)
        val list = repository.findAllByAtualizacaoGreaterThanEqual(dateTime, pageable).map { addLink(toDto(it)) }
        val link = linkTo(methodOn(clazzController).getLastSyncPage(updateDate, list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
        return assembler.toModel(list, link)
    }

    private fun getById(id: ID): E = repository.select(id).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }

    private fun findAll(): List<E> = repository.findAll()

    private fun findAllByAtualizacaoGreaterThanEqual(updateDate: LocalDateTime): List<E> = repository.findAllByAtualizacaoGreaterThanEqual(updateDate)

    operator fun get(id: ID): D = addLink(toDto(getById(id)))

    open fun getAll(table: String): List<D> = addLink(toDto(findAll()))

    fun getAll(updateDate: LocalDateTime): List<D> = addLink(toDto(findAllByAtualizacaoGreaterThanEqual(updateDate)))

    @Transactional
    open fun update(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        
        val entity = toEntity(dto)
        val dbEntity = getById((entity as Entity<ID, E>).getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(toDto(repository.update(dbEntity)))
    }

    @Transactional
    open fun update(dtos: List<D>): List<D> {
        
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity = getById((it as Entity<ID, E>).getId())
            (dbEntity as Entity<ID, E>).merge(it)
            saved.add(toDto(repository.update(dbEntity)))
        }
        return addLink(saved)
    }

    @Transactional
    open fun create(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        val entity = toEntity(dto)
        val dbEntity: E = (entity as Entity<ID, E>).create(entity.getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(toDto(repository.insert(dbEntity)))
    }

    @Transactional
    open fun create(dtos: List<D>): List<D> {
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = (it as Entity<ID, E>).create(it.getId())
            (dbEntity as Entity<ID, E>).merge(it)
            saved.add(toDto(repository.insert(dbEntity)))
        }
        return addLink(saved)
    }

    @Transactional
    open fun patch(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        try {
            val entity = toEntity(dto)
            val dbEntity = getById((entity as Entity<ID, E>).getId())
            (dbEntity as Entity<ID, E>).patch(entity)
            return addLink(toDto(repository.update(dbEntity)))
        } catch (e: Exception) {
            ServiceJpaBase.oLog.error("Error patch item on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun patch(dtos: List<D>): List<D> {
        try {
            val entities = toEntity(dtos)
            val saved = mutableListOf<D>()
            entities.forEach {
                val dbEntity = getById((it as Entity<ID, E>).getId())
                (dbEntity as Entity<ID, E>).patch(it)
                saved.add(toDto(repository.update(dbEntity)))
            }
            return addLink(saved)
        } catch (e: Exception) {
            ServiceJpaBase.oLog.error("Error patch item list on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun delete(id: ID) {
        get(id)
        repository.delete(id)
    }

    open fun delete(delete: List<ID>) = delete.forEach { delete(it) }
    open fun delete(obj: D) = delete(obj.getId())

    private fun addLink(obj : D) : D = obj.let { it.add(linkTo(methodOn(clazzController).getOne(it.getId())).withSelfRel()); it}
    private fun addLink(list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(it) }; l }

    fun toDto(obj: E): D = Mapper.parse(obj, clazzDto)
    fun toDto(list: Page<E>): Page<D> = Mapper.parse(list, clazzDto)
    fun toDto(list: List<E>): List<D> = Mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = Mapper.parse(obj, clazzEntity)
    fun toEntity(list: Page<D>): Page<E> = Mapper.parse(list, clazzEntity)
    fun toEntity(list: List<D>): List<E> = Mapper.parse(list, clazzEntity)
}