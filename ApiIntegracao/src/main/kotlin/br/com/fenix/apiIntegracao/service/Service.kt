package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.mapper.Mapper
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


abstract class Service<ID, E : Entity<E, ID>, D>(var repository: Repository<E, ID>, val clazzEntity: Class<E>, val clazzDto: Class<D>) {

    fun getPage(pageable: Pageable?): Page<D> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return toDto(repository.findAll(pageable))
    }

    fun getPage(atualizacao: LocalDateTime, pageable: Pageable): Page<D> {
        return toDto(repository.findAllByAtualizacaoGreaterThanEqual(atualizacao, pageable))
    }

    private fun getById(id: ID): E {
        return repository.findById(id).orElseThrow { InvalidAuthenticationException("Recurso de $id não encontrado.") }
    }

    operator fun get(id: ID): D {
        return toDto(getById(id))
    }

    fun getAll(): List<D> = toDto(repository.findAll())

    fun getAll(atualizacao: LocalDateTime): List<D> = toDto(repository.findAllByAtualizacaoGreaterThanEqual(atualizacao))

    @Transactional
    open fun update(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        val entity = toEntity(dto)
        val dbEntity = getById((entity as Entity<E, ID>).getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return toDto(repository.save(dbEntity))
    }

    @Transactional
    open fun create(dto: D?): D {
        if (dto == null)
            throw RequiredObjectIsNullException()
        val entity = toEntity(dto)
        val dbEntity: E = (entity as Entity<E, ID>).create(entity.getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return toDto(repository.save(dbEntity))
    }

    @Transactional
    open fun delete(id: ID) {
        get(id)
        repository.deleteById(id)
    }

    fun toDto(obj: E): D = Mapper.parse(obj, clazzDto)
    fun toDto(obj: Page<E>): Page<D> = Mapper.parse(obj, clazzDto)
    fun toDto(obj: List<E>): List<D> = Mapper.parse(obj, clazzDto)

    fun toEntity(obj: D): E = Mapper.parse(obj, clazzEntity)
    fun toEntity(obj: Page<D>): Page<E> = Mapper.parse(obj, clazzEntity)
    fun toEntity(obj: List<D>): List<E> = Mapper.parse(obj, clazzEntity)
}