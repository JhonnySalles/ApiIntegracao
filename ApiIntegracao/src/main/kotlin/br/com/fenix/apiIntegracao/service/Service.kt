package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.exceptions.ResourceNotFoundException
import br.com.fenix.apiIntegracao.mapper.Mapper
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


abstract class Service<E : Entity<E, ID>, ID, D>(var repository: Repository<E, ID>, var entityClass: Class<E>, var dtoClass: Class<D>) {

    fun getPage(pageable: Pageable): Page<D> {
        return Mapper.parse(repository.findAll(pageable), dtoClass)
    }

    fun getPage(sync : LocalDateTime, pageable: Pageable): Page<D> {
        return Mapper.parse(repository.lastSync(sync, pageable), dtoClass)
    }

    private fun getById(id: ID): E {
        return repository.findById(id).orElseThrow { ResourceNotFoundException("Recurso de $id não encontrado.") }
    }

    operator fun get(id: ID): D {
        return Mapper.parse(getById(id), dtoClass)
    }

    fun getAll(): List<D> = Mapper.parse(repository.findAll(), dtoClass)

    fun getAll(sync : LocalDateTime): List<D> = Mapper.parse(repository.lastSync(sync), dtoClass)

    @Transactional
    fun update(dto: D): D {
        val entity = Mapper.parse(dto, entityClass)
        val dbEntity = getById((entity as Entity<E, ID>).getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return Mapper.parse(repository.save(dbEntity), dtoClass)
    }

    @Transactional
    fun create(dto: D): D {
        val entity = Mapper.parse(dto, entityClass)
        val dbEntity: E = (entity as Entity<E, ID>).create(entity.getId())
        (dbEntity as Entity<E, ID>).merge(entity)
        return Mapper.parse(repository.save(dbEntity), dtoClass)
    }

    @Transactional
    fun delete(id: ID) {
        get(id)
        repository.deleteById(id)
    }
}