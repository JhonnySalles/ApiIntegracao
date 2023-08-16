package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.converters.MediaTypes
import br.com.fenix.apiIntegracao.dto.BaseDto
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


abstract class Controller<ID, E : Entity<E, ID>, D : BaseDto<ID>, C : Controller<ID, E, D, C>>(repository: Repository<E, ID>) {
    private val service: Service<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        service = object : Service<ID, E, D, C>(repository, clazzEntity, clazzDto, clazzController) {}
    }

    @Operation(summary = "Pesquisa paginada", description = "Pesquisa paginada com retorno em JSON, XMl, YML e CSV.")
    @GetMapping(
        "",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,
        ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,
        )
    )
    fun getPage(pageable: Pageable?): ResponseEntity<Page<D>> {
        return ResponseEntity.ok(service.getPage(pageable))
    }

    @Operation(summary = "Pesquisa paginada apartir da data informada", description = "Pesquisa paginada apartir da data informada")
    @GetMapping(
        "/{atualizacao}",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun getLastSyncPage(@PathVariable atualizacao: String, pageable: Pageable): ResponseEntity<Page<D>> {
        return ResponseEntity.ok(service.getPage(LocalDateTime.parse(atualizacao), pageable))
    }

    @Operation(summary = "Pesquisa por id", description = "Pesquisa por id")
    @GetMapping(
        "/{id}",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun getOne(@PathVariable id: ID): ResponseEntity<D> {
        return ResponseEntity.ok(service[id])
    }

    @Operation(summary = "Pesquisar todos", description = "Pesquisar todos")
    @GetMapping(
        "/findAll",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun getAll(): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.getAll())
    }

    @Operation(summary = "Pesquisar todos apartir da data informada", description = "Pesquisar todos apartir da data informada")
    @GetMapping(
        "/findAll/{atualizacao}",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun getAllLastSync(@PathVariable atualization: String): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.getAll(LocalDateTime.parse(atualization)))
    }

    @Operation(summary = "Atualizar registro", description = "Atualizar registro")
    @PutMapping(
        "",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun update(@RequestBody update: D?): ResponseEntity<D> {
        return ResponseEntity.ok(service.update(update))
    }

    @Operation(summary = "Atualizar vários registros", description = "Atualizar vários registros")
    @PutMapping(
        "/updateAll/",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun update(@RequestBody update: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.update(update))
    }

    @Operation(summary = "Inserir registro", description = "Inserir registro")
    @PostMapping(
        "",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun create(@RequestBody create: D?): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(create))
    }

    @Operation(summary = "Inserir vários registros", description = "Inserir vários registros")
    @PostMapping(
        "/createAll/",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun create(@RequestBody create: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.create(create))
    }

    @Operation(summary = "Deletar registro", description = "Deletar registro")
    @DeleteMapping(
        "/{id}",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun delete(@PathVariable id: ID): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }

    @Operation(summary = "Deletar vários registros", description = "Deletar vários registros")
    @DeleteMapping(
        "/deleteAll/",
        consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            ),
        produces = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaTypes.MEDIA_TYPE_APPLICATION_YML_VALUE,

            )
    )
    fun delete(@RequestBody delete: List<ID>): ResponseEntity<String> {
        service.delete(delete)
        return ResponseEntity.ok("Ok")
    }
}