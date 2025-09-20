package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemFull
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.TableNotExistsException
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemFull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.transaction.annotation.Transactional

abstract class ServiceJdbcItemFull<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseItemFull<ID, E, D, C>>(
    var repo: RepositoryJdbcItemFull<E, ID>, val factory: EntityFactory<ID, E>, val clazzEntity: Class<E>, val clazzDto: Class<D>, val clazzController: Class<C>
) {

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(ServiceJdbcItemFull::class.java)
    }

    abstract val mapper : Mapper

    private fun existTable(table: String) : Boolean = repo.existstabela(table)

    fun validTable(table: String) {
        if (!existTable(table))
            throw TableNotExistsException()
    }

    private fun findAll(table: String, idParent : ID): List<E> {
        validTable(table)
        return repo.findAll(table, idParent)
    }

    fun getAll(table: String, idParent : ID): List<D> = addLink(table, toDto(findAll(table, idParent)))

    @Transactional
    open fun update(table: String, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        validTable(table)
        val entity = toEntity(dto)
        val dbEntity = repo.select(table, (entity as Entity<ID, E>).getId()).orElseThrow { NotFoundException() }
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(table, toDto(repo.update(table, dbEntity)))
    }

    @Transactional
    open fun update(table: String, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            repo.select(table, (it as Entity<ID, E>).getId()).ifPresent { dbEntity ->
                (dbEntity as Entity<ID, E>).merge(it)
                saved.add(toDto(repo.update(table, dbEntity)))
            }
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun create(table: String, idParent : ID, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        validTable(table)
        val entity = toEntity(dto)
        val dbEntity: E = factory.create(entity.getId())
        (dbEntity as Entity<ID, E>).merge(entity)
        return addLink(table, toDto(repo.insert(table, idParent, dbEntity)))
    }

    @Transactional
    open fun create(table: String, idParent : ID, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            val dbEntity: E = factory.create(it.getId())
            (dbEntity as Entity<ID, E>).merge(it)
            saved.add(toDto(repo.insert(table, idParent, dbEntity)))
        }
        return addLink(table, saved)
    }

    @Transactional
    open fun delete(table: String, id: ID) = repo.delete(table, id)

    open fun delete(table: String, delete: List<ID>) = delete.forEach { delete(table, it) }

    private fun addLink(table: String, obj : D) : D = obj.let { it.add(linkTo(clazzController).slash(obj.getId()).withSelfRel()); it}
    private fun addLink(table: String, list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(table, it) }; l }

    fun toDto(obj: E): D = mapper.parse(obj, clazzDto)
    fun toDto(list: List<E>): List<D> = mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = mapper.parse(obj, clazzEntity)
    fun toEntity(list: List<D>): List<E> = mapper.parse(list, clazzEntity)

}