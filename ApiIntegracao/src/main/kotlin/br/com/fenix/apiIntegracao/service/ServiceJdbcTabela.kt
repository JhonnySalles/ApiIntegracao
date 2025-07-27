package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJdbc
import br.com.fenix.apiintegracao.controller.ControllerJdbcTabela
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.TableNotExistsException
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

abstract class ServiceJdbcTabela<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbc<ID, E, D, C>>(
    var repo: RepositoryJdbcTabela<E, ID>, override val clazzEntity: Class<E>, override val clazzDto: Class<D>, override val clazzController: Class<C>
) : ServiceJdbcBase<ID, E, D, C>(repo, clazzEntity, clazzDto, clazzController) {

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(ServiceJdbcTabela::class.java)
    }

    fun getTables() : List<String> = repo.tabelas()

    private fun existTable(table: String) : Boolean = repo.existstabela(table)

    private fun createTable(table: String) = repo.createtabela(table)

    private fun verifyTable(table: String) {
        if (!existTable(table))
            createTable(table)
    }

    fun validTable(table: String) {
        if (!existTable(table))
            throw TableNotExistsException()
    }

    override fun getPage(table: String, pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        validTable(table)
        try {
            val list = repo.findAll(table, pageable).map { addLink(table, toDto(it)) }
            val link = linkTo(methodOn(clazzController).getPage(table, list.pageable.pageNumber, list.pageable.pageSize, "asc", assembler)).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            oLog.error("Error get page on jpa base", e)
            throw e
        }
    }

    fun getPage(table: String, updateDate: String, pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        validTable(table)
        val dateTime = LocalDateTime.parse(updateDate)
        val list = repo.findAllByAtualizacaoGreaterThanEqual(table, dateTime, pageable).map { addLink(table, toDto(it)) }
        val link = linkTo(methodOn(clazzController).getLastSyncPage(table, updateDate, list.pageable.pageNumber, list.pageable.pageSize, "asc", assembler)).withSelfRel()
        return assembler.toModel(list, link)
    }

    private fun getById(table: String, id: ID): E {
        validTable(table)
        return repo.select(table, id).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }
    }

    private fun findAll(table: String): List<E> {
        validTable(table)
        return repo.findAll(table)
    }

    private fun findAllByAtualizacaoGreaterThanEqual(table: String, updateDate: LocalDateTime): List<E> {
        validTable(table)
        return repo.findAllByAtualizacaoGreaterThanEqual(table, updateDate)
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
        val dbEntity = getById(table, (entity as Entity<ID, E>).getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(table, toDto(repo.update(table, dbEntity)))
    }

    @Transactional
    open fun update(table: String, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity = getById(table, (it as Entity<ID, E>).getId())
            (dbEntity as Entity<ID, E>).merge(it)
            saved.add(toDto(repo.update(table, dbEntity)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun create(table: String, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        verifyTable(table)
        val entity = toEntity(dto)
        val dbEntity: E = (entity as Entity<ID, E>).create(entity.getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(table, toDto(repo.insert(table, dbEntity)))
    }

    @Transactional
    open fun create(table: String, dtos: List<D>): List<D> {
        verifyTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = (it as Entity<ID, E>).create(it.getId())
            (dbEntity as Entity<ID, E>).merge(it)
            saved.add(toDto(repo.insert(table, dbEntity)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun delete(table: String, id: ID) {
        get(table, id)
        repo.delete(table, id)
    }

    open fun delete(table: String, delete: List<ID>) = delete.forEach { delete(table, it) }

    private fun addLink(table: String, obj : D) : D = obj.let { it.add(linkTo(methodOn(clazzController).getOne(table, it.getId())).withSelfRel()); it}
    private fun addLink(table: String, list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(table, it) }; l }

}