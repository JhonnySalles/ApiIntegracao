package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.dto.DtoBase
import br.com.fenix.apiIntegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.mapper.Mapper
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.model.EntityBase
import br.com.fenix.apiIntegracao.repository.RepositoryJpaBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


abstract class ServiceJpaBase<ID, E : EntityBase<E, ID>, D : DtoBase<ID>, C : ControllerJpaBase<ID, E, D, C>>(var repository: RepositoryJpaBase<E, ID>, var assembler: PagedResourcesAssembler<D>, val clazzEntity: Class<E>, val clazzDto: Class<D>, val clazzController: Class<C>) {

    fun getPage(pageable: Pageable?): PagedModel<EntityModel<D>> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        val list = repository.findAll(pageable).map { addLink(toDto(it)) }
        val link = linkTo(methodOn(clazzController).getPage(list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
        return assembler.toModel(list, link)
    }

    fun getPage(updateDate: String, pageable: Pageable): PagedModel<EntityModel<D>> {
        val dateTime = LocalDateTime.parse(updateDate)
        val list = repository.findAllByAtualizacaoGreaterThanEqual(dateTime, pageable).map { addLink(toDto(it)) }
        val link = linkTo(methodOn(clazzController).getLastSyncPage(updateDate, list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
        return assembler.toModel(list, link)
    }

    private fun getById(id: ID): E {
        return repository.findById(id).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }
    }

    operator fun get(id: ID): D = addLink(toDto(getById(id)))

    fun getAll(): List<D> = addLink(toDto(repository.findAll()))

    fun getAll(updateDate: LocalDateTime): List<D> = addLink(toDto(repository.findAllByAtualizacaoGreaterThanEqual(updateDate)))

    @Transactional
    open fun update(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        val entity = toEntity(dto)
        val dbEntity = getById((entity as Entity<E, ID>).getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return addLink(toDto(repository.save(dbEntity)))
    }

    @Transactional
    open fun update(dtos: List<D>): List<D> {
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity = getById((it as Entity<E, ID>).getId())
            (dbEntity as Entity<E, ID>).merge(it)
            saved.add(toDto(repository.save(dbEntity)))
        }
        return addLink(saved)
    }

    @Transactional
    open fun create(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        val entity = toEntity(dto)
        val dbEntity: E = (entity as Entity<E, ID>).create(entity.getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return addLink(toDto(repository.save(dbEntity)))
    }

    @Transactional
    open fun create(dtos: List<D>): List<D> {
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = (it as Entity<E, ID>).create(it.getId())
            (dbEntity as Entity<E, ID>).merge(it)
            saved.add(toDto(repository.save(dbEntity)))
        }
        return addLink(saved)
    }

    @Transactional
    open fun delete(id: ID) {
        get(id)
        repository.deleteById(id)
    }

    open fun delete(delete: List<ID>) = delete.forEach { delete(it) }

    private fun addLink(obj : D) : D = obj.let { it.add(linkTo(methodOn(clazzController).getOne(it.getId())).withSelfRel()); it}
    private fun addLink(list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(it) }; l }

    fun toDto(obj: E): D = Mapper.parse(obj, clazzDto)
    fun toDto(list: Page<E>): Page<D> = Mapper.parse(list, clazzDto)
    fun toDto(list: List<E>): List<D> = Mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = Mapper.parse(obj, clazzEntity)
    fun toEntity(list: Page<D>): Page<E> = Mapper.parse(list, clazzEntity)
    fun toEntity(list: List<D>): List<E> = Mapper.parse(list, clazzEntity)
}