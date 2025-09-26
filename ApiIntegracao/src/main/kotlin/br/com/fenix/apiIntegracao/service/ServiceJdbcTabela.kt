package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.TableNotExistsException
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import br.com.fenix.apiintegracao.utils.Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

abstract class ServiceJdbcTabela<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseTabela<ID, E, D, C>>(
    var repo: RepositoryJdbcTabela<E, ID>,
    override val factory: EntityFactory<ID, E>,
    override val clazzEntity: Class<E>,
    override val clazzDto: Class<D>,
    override val clazzController: Class<C>
) : ServiceJdbcBase<ID, E, D, C>(repo, factory, clazzEntity, clazzDto, clazzController) {

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(ServiceJdbcTabela::class.java)
    }

    fun getTables(): List<String> = repo.tabelas()

    private fun existTable(table: String): Boolean = repo.existstabela(table)

    private fun createTable(table: String) = repo.createtabela(table)

    private fun verifyTable(table: String) {
        if (!existTable(table))
            createTable(table)
    }

    fun validTable(table: String) {
        if (!existTable(table))
            throw TableNotExistsException()
    }

    fun getPage(table: String, pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        validTable(table)
        try {
            val list = toDtoLink(table, repo.findAll(table, pageable))
            val link = linkTo(clazzController).withSelfRel()
            return assembler.toModel(list, link)
        } catch (e: Exception) {
            oLog.error("Error get page on jpa base", e)
            throw e
        }
    }

    fun getLastSyncPage(table: String, updateDate: String, pageable: Pageable, assembler: PagedResourcesAssembler<D>): PagedModel<EntityModel<D>> {
        validTable(table)
        val dateTime: LocalDateTime = Utils.updateDateToLocalDateTime(updateDate)
        val list = toDtoLink(table, repo.findAllByAtualizacaoGreaterThanEqual(table, dateTime, pageable))
        val link = linkTo(clazzController).slash(ATUALIZACAO).slash(updateDate).withSelfRel()
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

    private fun saveOrUpdate(table: String, id: ID?, entity: E): E {
        val idEntity = if (id != null)
            id
        else
            (entity as Entity<ID, E>).getId()
        val dbEntity = if (idEntity == null)
            Optional.empty()
        else
            repo.select(table, idEntity)

        val newEntity = if (dbEntity.isPresent)
            dbEntity.get()
        else
            factory.create(idEntity)

        (newEntity as Entity<ID, E>).merge(entity)
        return if (dbEntity.isPresent)
            repo.update(table, newEntity)
        else
            repo.insert(table, newEntity)
    }

    @Transactional
    open fun update(table: String, id: ID, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        validTable(table)
        return addLink(table, toDto(saveOrUpdate(table, id, toEntity(dto))))
    }

    @Transactional
    open fun update(table: String, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            saved.add(toDto(saveOrUpdate(table, (it as Entity<ID, E>).getId(), it)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun create(table: String, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        verifyTable(table)
        val entity = toEntity(dto)
        val dbEntity: E = factory.create(entity.getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(table, toDto(repo.insert(table, dbEntity)))
    }

    @Transactional
    open fun create(table: String, dtos: List<D>): List<D> {
        verifyTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = factory.create(it.getId())
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

    private fun addLink(table: String, obj: D): D = obj.let { it.add(linkTo(clazzController).slash(obj.getId()).withSelfRel()); it }
    private fun addLink(table: String, list: List<D>): List<D> = list.let { l -> l.parallelStream().forEach { addLink(table, it) }; l }
    private fun addLink(table: String, list: Page<D>): Page<D> = list.map { addLink(table, it) }
    private fun toDtoLink(table: String, list: Page<E>): Page<D> = list.map { addLink(table, toDto(it)) }

}