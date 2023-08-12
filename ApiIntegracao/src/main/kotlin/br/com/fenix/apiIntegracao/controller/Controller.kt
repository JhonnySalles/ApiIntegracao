package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.model.Entity
import br.com.fenix.apiIntegracao.repository.Repository
import br.com.fenix.apiIntegracao.service.Service
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType
import java.time.LocalDateTime


abstract class Controller<ID, E : Entity<E, ID>, D>(repository: Repository<E, ID>) {
    private val service: Service<ID, E, D>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        service = object : Service<ID, E, D>(repository, clazzEntity, clazzDto) {}
    }

    @Operation(summary = "Pesquisa paginada", description = "Pesquisa paginada")
    @GetMapping("", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getPage(pageable: Pageable?): ResponseEntity<Page<D>> {
        return ResponseEntity.ok(service.getPage(pageable))
    }

    @Operation(summary = "Pesquisa paginada apartir da data informada", description = "Pesquisa paginada apartir da data informada")
    @GetMapping("/{atualizacao}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getLastSyncPage(@PathVariable atualizacao: String, pageable: Pageable): ResponseEntity<Page<D>> {
        return ResponseEntity.ok(service.getPage(LocalDateTime.parse(atualizacao), pageable))
    }

    @Operation(summary = "Pesquisa por id", description = "Pesquisa por id")
    @GetMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getOne(@PathVariable id: ID): ResponseEntity<D> {
        return ResponseEntity.ok(service[id])
    }

    @Operation(summary = "Pesquisar todos", description = "Pesquisar todos")
    @GetMapping("/findAll", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getAll(): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.getAll())
    }

    @Operation(summary = "Pesquisar todos apartir da data informada", description = "Pesquisar todos apartir da data informada")
    @GetMapping("/findAll/{atualizacao}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getAllLastSync(@PathVariable atualizacao: String): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.getAll(LocalDateTime.parse(atualizacao)))
    }

    @Operation(summary = "Atualizar registro", description = "Atualizar registro")
    @PutMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun update(@RequestBody updated: D?): ResponseEntity<D> {
        return ResponseEntity.ok(service.update(updated))
    }

    @Operation(summary = "Inserir registro", description = "Inserir registro")
    @PostMapping(
        "", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun create(@RequestBody created: D?): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(created))
    }

    @Operation(summary = "Deletar registro", description = "Deletar registro")
    @DeleteMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun delete(@PathVariable id: ID): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }
}