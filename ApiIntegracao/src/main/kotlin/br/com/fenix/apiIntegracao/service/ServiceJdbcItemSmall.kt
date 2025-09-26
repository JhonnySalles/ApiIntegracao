package br.com.fenix.apiintegracao.service

import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemSmall
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.exceptions.TableNotExistsException
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.Entity
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemSmall
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.transaction.annotation.Transactional
import java.util.*

abstract class ServiceJdbcItemSmall<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseItemSmall<ID, E, D, C>>(
    var repo: RepositoryJdbcItemSmall<E, ID>, val factory: EntityFactory<ID, E>, val clazzEntity: Class<E>, val clazzDto: Class<D>, val clazzController: Class<C>
) {

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(ServiceJdbcItemSmall::class.java)
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

    private fun saveOrUpdate(table: String, idParent: ID, id: ID?, entity: E): E {
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
            repo.insert(table, idParent, newEntity)
    }

    @Transactional
    open fun update(table: String, idParent: ID, id: ID, dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        validTable(table)
        return addLink(table, toDto(saveOrUpdate(table, idParent, id, toEntity(dto))))
    }

    @Transactional
    open fun update(table: String, idParent: ID, dtos: List<D>): List<D> {
        validTable(table)
        val entities = toEntity(dtos)
        val saved = mutableListOf<D>()
        entities.forEach {
            saved.add(toDto(saveOrUpdate(table, idParent, it.getId(), it)))
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

    private fun addLink(table: String, obj : D) : D = obj.let { it.add(linkTo(clazzController).slash(obj.getId()).withSelfRel()); it}
    private fun addLink(table: String, list : List<D>) : List<D> = list.let { l -> l.parallelStream().forEach{ addLink(table, it) }; l }

    fun toDto(obj: E): D = mapper.parse(obj, clazzDto)
    fun toDto(list: List<E>): List<D> = mapper.parse(list, clazzDto)

    fun toEntity(obj: D): E = mapper.parse(obj, clazzEntity)
    fun toEntity(list: List<D>): List<E> = mapper.parse(list, clazzEntity)

}