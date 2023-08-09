package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import br.com.fenix.apiIntegracao.service.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

    @Operation(summary = "Pesquisa paginada", description = "Pesquisa paginada")
    @GetMapping("", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getPage(pageable: Pageable): ResponseEntity<Page<T>> {
        return ResponseEntity.ok(service.getPage(pageable))
    }

    @Operation(summary = "Pesquisa por id", description = "Pesquisa por id")
    @GetMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getOne(@PathVariable id: ID): ResponseEntity<T> {
        return ResponseEntity.ok(service[id])
    }

    @Operation(summary = "Pesquisar todos", description = "Pesquisar todos")
    @GetMapping("/findAll", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getAll(): ResponseEntity<List<T>> {
        return ResponseEntity.ok(service.getAll())
    }

    @Operation(summary = "Atualizar registro", description = "Atualizar registro")
    @PutMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun update(@RequestBody updated: T): ResponseEntity<T> {
        return ResponseEntity.ok(service.update(updated))
    }

    @Operation(summary = "Inserir registro", description = "Inserir registro")
    @PostMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun create(@RequestBody created: T): ResponseEntity<T> {
        return ResponseEntity.ok(service.create(created))
    }

    @Operation(summary = "Deletar registro", description = "Deletar registro")
    @DeleteMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun delete(@PathVariable id: ID): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }
}