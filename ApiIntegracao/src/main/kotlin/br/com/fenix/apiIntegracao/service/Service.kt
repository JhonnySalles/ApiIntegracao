package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.exceptions.ResourceNotFoundException
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional


abstract class Service<T : Entity<T, ID>, ID>(repository: Repository<T, ID>) {
    private val repository: Repository<T, ID>

    init {
        this.repository = repository
    }

    fun getPage(pageable: Pageable): Page<T> {
        return repository.findAll(pageable)
    }

    operator fun get(id: ID): T {
        return repository.findById(id).orElseThrow { ResourceNotFoundException("Recurso de $id não encontrado.") }
    }

    fun getAll(): List<T> {
        return repository.findAll()
    }

    @Transactional
    fun update(entity: T): T {
        val dbEntity = get((entity as Entity<T, ID>).getId())
        (dbEntity as Entity<T, ID>).merge(entity)
        return repository.save(dbEntity)
    }

    @Transactional
    fun create(entity: T): T {
        val dbEntity: T = (entity as Entity<T, ID>).create(entity.getId())
        return repository.save(dbEntity)
    }

    @Transactional
    fun delete(id: ID) {
        get(id)
        repository.deleteById(id)
    }
}