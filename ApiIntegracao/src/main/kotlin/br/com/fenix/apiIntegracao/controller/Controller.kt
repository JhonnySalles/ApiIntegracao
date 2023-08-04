package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import br.com.fenix.apiIntegracao.service.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


abstract class Controller<T : Entity<T, ID>, ID>(repository: Repository<T, ID>) {
    private val service: Service<T, ID>

    init {
        service = object : Service<T, ID>(repository) {}
    }

    @GetMapping("", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getPage(pageable: Pageable): ResponseEntity<Page<T>> {
        return ResponseEntity.ok(service.getPage(pageable))
    }

    @GetMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getOne(@PathVariable id: ID): ResponseEntity<T> {
        return ResponseEntity.ok(service[id])
    }

    @GetMapping("/findAll", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getAll(): ResponseEntity<List<T>> {
        return ResponseEntity.ok(service.getAll())
    }

    @PutMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun update(@RequestBody updated: T): ResponseEntity<T> {
        return ResponseEntity.ok(service.update(updated))
    }

    @PostMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun create(@RequestBody created: T): ResponseEntity<T> {
        return ResponseEntity.ok(service.create(created))
    }

    @DeleteMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun delete(@PathVariable id: ID): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }
}