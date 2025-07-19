package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.controller.ControllerJdbcBase
import br.com.fenix.apiIntegracao.dto.DtoBase
import br.com.fenix.apiIntegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.exceptions.TableNotExistsException
import br.com.fenix.apiIntegracao.mapper.Mapper
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.model.EntityBase
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


abstract class ServiceJdbcBase<ID, E : EntityBase<E, ID>, D : DtoBase<ID>, C : ControllerJdbcBase<ID, E, D, C>>(
    var repository: RepositoryJdbcBase<E, ID>, var assembler: PagedResourcesAssembler<D>, val clazzEntity: Class<E>, val clazzDto: Class<D>, val clazzController: Class<C>
) {

    fun getTables() : List<String> = repository.tabelas()

    private fun existTable(table: String) : Boolean = repository.existstabela(table)

    private fun createTable(table: String) = repository.createtabela(table)

    private fun verifyTable(table: String) {
        if (!existTable(table))
            createTable(table)
    }

    fun validTable(table: String) {
        if (!existTable(table))
            throw TableNotExistsException()
    }

    fun getPage(table: String, pageable: Pageable?): PagedModel<EntityModel<D>> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        validTable(table)
        try {
            val list = repository.findAll(table, pageable).map { addLink(table, toDto(it)) }
            val link = linkTo(methodOn(clazzController).getPage(table, list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun getPage(table: String, updateDate: String, pageable: Pageable): PagedModel<EntityModel<D>> {
        validTable(table)
        val dateTime = LocalDateTime.parse(updateDate)
        val list = repository.findAllByAtualizacaoGreaterThanEqual(table, dateTime, pageable).map { addLink(table, toDto(it)) }
        val link = linkTo(methodOn(clazzController).getLastSyncPage(table, updateDate, list.pageable.pageNumber, list.pageable.pageSize, "asc")).withSelfRel()
        return assembler.toModel(list, link)
    }

    private fun getById(table: String, id: ID): E {
        validTable(table)
        return repository.select(table, id).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }
    }

    private fun findAll(table: String): List<E> {
        validTable(table)
        return repository.findAll(table)
    }

    private fun findAllByAtualizacaoGreaterThanEqual(table: String, updateDate: LocalDateTime): List<E> {
        validTable(table)
        return repository.findAllByAtualizacaoGreaterThanEqual(table, updateDate)
    }

    operator fun get(table: String, id: ID): D = addLink(table, toDto(getById(table, id)))

    fun getAll(table: String): List<D> = addLink(table, toDto(findAll(table)))

    fun getAll(table: String, updateDate: LocalDateTime): List<D> = addLink(table, toDto(findAllByAtualizacaoGreaterThanEqual(table, updateDate)))

    @Transactional
    open fun update(table: String, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        validTable(table)
        val entity = toEntity(dto)
        val dbEntity = getById(table, (entity as Entity<E, ID>).getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return addLink(table, toDto(repository.update(table, dbEntity)))
    }

    @Transactional
    open fun update(table: String, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity = getById(table, (it as Entity<E, ID>).getId())
            (dbEntity as Entity<E, ID>).merge(it)
            saved.add(toDto(repository.update(table, dbEntity)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun create(table: String, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        verifyTable(table)
        val entity = toEntity(dto)
        val dbEntity: E = (entity as Entity<E, ID>).create(entity.getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return addLink(table, toDto(repository.insert(table, dbEntity)))
    }

    @Transactional
    open fun create(table: String, dtos: List<D>): List<D> {
        verifyTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = (it as Entity<E, ID>).create(it.getId())
            (dbEntity as Entity<E, ID>).merge(it)
            saved.add(toDto(repository.insert(table, dbEntity)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun delete(table: String, id: ID) {
        get(table, id)
        repository.delete(table, id)
    }

    open fun delete(table: String, delete: List<ID>) = delete.forEach { delete(table, it) }

    private fun addLink(table: String, obj : D) : D = obj.let { it.add(linkTo(methodOn(clazzController).getOne(table, it.getId())).withSelfRel()); it}
    private fun addLink(table: String, list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(table, it) }; l }

    fun toDto(obj: E): D = Mapper.parse(obj, clazzDto)
    fun toDto(list: Page<E>): Page<D> = Mapper.parse(list, clazzDto)
    fun toDto(list: List<E>): List<D> = Mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = Mapper.parse(obj, clazzEntity)
    fun toEntity(list: Page<D>): Page<E> = Mapper.parse(list, clazzEntity)
    fun toEntity(list: List<D>): List<E> = Mapper.parse(list, clazzEntity)
}