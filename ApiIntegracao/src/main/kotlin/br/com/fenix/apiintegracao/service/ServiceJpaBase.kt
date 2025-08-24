package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.ServerErrorException
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJpaBase
import br.com.fenix.apiintegracao.utils.Utils
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

abstract class ServiceJpaBase<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJpaBase<ID, E, D, C, R>, R : RepositoryJpaBase<E, ID>>(open val factory: EntityFactory<ID, E>, val clazzEntity: Class<E>, val clazzDto: Class<D>, val clazzController: Class<C>) {
    companion object {
        val oLog = LoggerFactory.getLogger(ServiceJpaBase::class.java.name)
    }

    abstract val repository: RepositoryJpaBase<E, ID>
    abstract val mapper : Mapper

    fun getPage(pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        try {
            val list = toDtoLink(repository.findAll(pageable))
            val link = linkTo(clazzController).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            oLog.error("Error get page on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    fun getLastSyncPage(updateDate: String, pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        try {
            val dateTime : LocalDateTime = Utils.updateDateToLocalDateTime(updateDate)
            val list = toDtoLink(repository.findAllByAtualizacaoGreaterThanEqual(dateTime, pageable))
            val link = linkTo(clazzController).slash(ATUALIZACAO).slash(updateDate).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            oLog.error("Error get page on jpa base with update date", e)
            throw ServerErrorException(e.message)
        }
    }

    private fun getById(id: ID): E {
        return repository.findById(id!!).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }
    }

    operator fun get(id: ID): D = addLink(toDto(getById(id)))

    fun getAll(): List<D> = addLink(toDto(repository.findAll()))

    fun getAll(updateDate: LocalDateTime): List<D> = addLink(toDto(repository.findAllByAtualizacaoGreaterThanEqual(updateDate)))

    @Transactional
    open fun update(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        try {
            val entity = toEntity(dto)
            val dbEntity = getById((entity as Entity<ID, E>).getId())
            (dbEntity as Entity<ID, E>).merge(entity)
            return addLink(toDto(repository.save(dbEntity)))
        } catch (e: Exception) {
            oLog.error("Error update item on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun update(dtos: List<D>): List<D> {
        try {
            val entities = toEntity(dtos)
            val saved = mutableListOf<D>()
            entities.forEach {
                val dbEntity = getById((it as Entity<ID, E>).getId())
                (dbEntity as Entity<ID, E>).merge(it)
                saved.add(toDto(repository.save(dbEntity)))
            }
            return addLink(saved)
        } catch (e: Exception) {
            oLog.error("Error update item list on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun create(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        try {
            val entity = toEntity(dto)
            val dbEntity: E = factory.create(entity.getId())
            (dbEntity as Entity<ID, E>).merge(entity)
            return addLink(toDto(repository.save(dbEntity)))
        } catch (e: Exception) {
            oLog.error("Error create item on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun create(dtos: List<D>): List<D> {
        try {
            val entities = toEntity(dtos)
            val saved = mutableListOf<D>()
            entities.forEach {
                val dbEntity: E = factory.create(it.getId())
                (dbEntity as Entity<ID, E>).merge(it)
                saved.add(toDto(repository.save(dbEntity)))
            }
            return addLink(saved)
        } catch (e: Exception) {
            oLog.error("Error create item list on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun patch(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        try {
            val entity = toEntity(dto)
            val dbEntity = getById((entity as Entity<ID, E>).getId())
            (dbEntity as Entity<ID, E>).patch(entity)
            return addLink(toDto(repository.save(dbEntity)))
        } catch (e: Exception) {
            oLog.error("Error patch item on jpa base", e)
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
                saved.add(toDto(repository.save(dbEntity)))
            }
            return addLink(saved)
        } catch (e: Exception) {
            oLog.error("Error patch item list on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    @Transactional
    open fun delete(id: ID) {
        try {
            get(id)
            repository.deleteById(id!!)
        } catch (e: Exception) {
            oLog.error("Error delete item on jpa base", e)
            throw ServerErrorException(e.message)
        }
    }

    open fun delete(delete: List<ID>) = delete.forEach { delete(it) }
    open fun delete(obj: D) = delete(obj.getId())

    private fun addLink(obj: D): D = obj.let { it.add(linkTo(clazzController).slash(obj.getId()).withSelfRel()); it }
    private fun addLink(list: List<D>): List<D> = list.let { l -> l.parallelStream().forEach { addLink(it) }; l }
    private fun addLink(list: Page<D>): Page<D> = list.map { addLink(it) }
    private fun toDtoLink(list: Page<E>): Page<D> = list.map { addLink(toDto(it)) }

    fun toDto(obj: E): D = mapper.parse(obj, clazzDto)
    fun toDto(list: Page<E>): Page<D> = mapper.parse(list, clazzDto)
    fun toDto(list: List<E>): List<D> = mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = mapper.parse(obj, clazzEntity)
    fun toEntity(list: Page<D>): Page<E> = mapper.parse(list, clazzEntity)
    fun toEntity(list: List<D>): List<E> = mapper.parse(list, clazzEntity)
}