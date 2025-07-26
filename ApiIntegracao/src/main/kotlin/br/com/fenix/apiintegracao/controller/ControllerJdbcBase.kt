package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO_URL
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TABLES_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.repository.RepositoryJdbcBase
import br.com.fenix.apiintegracao.service.ServiceJdbcBase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType
import java.time.LocalDateTime

abstract class ControllerJdbcBase<ID, E : EntityBase<E, ID>, D : DtoBase<ID>, C : ControllerJdbcBase<ID, E, D, C>>(repository: RepositoryJdbcBase<E, ID>, assembler: PagedResourcesAssembler<D>) {
    private val service: ServiceJdbcBase<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        service = object : ServiceJdbcBase<ID, E, D, C>(repository, assembler, clazzEntity, clazzDto, clazzController) {}
    }

    @Operation(summary = "Tabelas disponíveis para a consulta.", description = "Tabelas disponíveis para a consulta.")
    @GetMapping(
        "/tables",
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
    fun getTables() : ResponseEntity<List<String>> {
        return ResponseEntity.ok(service.getTables())
    }

    @Operation(summary = "Pesquisa paginada", description = "Pesquisa paginada com retorno em JSON, XMl, YML e CSV.")
    @GetMapping(
        TABLES_URL,
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
    fun getPage(@PathVariable table: String, @RequestParam(value = "page", defaultValue = "0") page: Int,
                @RequestParam(value = "size", defaultValue = "1000") size: Int,
                @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        service.validTable(table)
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getPage(table, pageable))
    }

    @Operation(summary = "Pesquisa paginada apartir da data informada", description = "Pesquisa paginada apartir da data informada")
    @GetMapping(
        TABLES_URL + ATUALIZACAO_URL,
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
    fun getLastSyncPage(
        @PathVariable table: String, @PathVariable updateDate: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        service.validTable(table)
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getPage(table, updateDate, pageable))
    }

    @Operation(summary = "Pesquisa por id", description = "Pesquisa por id")
    @GetMapping(
        TABLES_URL + "/{id}",
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
    fun getOne(@PathVariable table: String, @PathVariable id: ID): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service[table, id])
    }

    @Operation(summary = "Pesquisar todos", description = "Pesquisar todos")
    @GetMapping(
        TABLES_URL + "/lista",
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
    fun getAll(@PathVariable table: String): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.getAll(table))
    }

    @Operation(summary = "Pesquisar todos apartir da data informada", description = "Pesquisar todos apartir da data informada")
    @GetMapping(
        TABLES_URL + "/lista" + ATUALIZACAO_URL,
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
    fun getAllLastSync(@PathVariable table: String, @PathVariable updateDate: String): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.getAll(table, LocalDateTime.parse(updateDate)))
    }

    @Operation(summary = "Atualizar registro", description = "Atualizar registro")
    @PutMapping(
        TABLES_URL,
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
    fun update(@PathVariable table: String, @RequestBody update: D?): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, update))
    }

    @Operation(summary = "Atualizar vários registros", description = "Atualizar vários registros")
    @PutMapping(
        TABLES_URL + "/lista/",
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
    fun update(@PathVariable table: String, @RequestBody update: List<D>): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, update))
    }

    @Operation(summary = "Inserir registro", description = "Inserir registro")
    @PostMapping(
        TABLES_URL,
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
    fun create(@PathVariable table: String, @RequestBody create: D?): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(table, create))
    }

    @Operation(summary = "Inserir vários registros", description = "Inserir vários registros")
    @PostMapping(
        TABLES_URL + "/lista/",
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
    fun create(@PathVariable table: String, @RequestBody create: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.create(table, create))
    }

    @Operation(summary = "Deletar registro", description = "Deletar registro")
    @DeleteMapping(
        TABLES_URL + "/{id}",
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
    fun delete(@PathVariable table: String, @PathVariable id: ID): ResponseEntity<String> {
        service.validTable(table)
        service.delete(table, id)
        return ResponseEntity.ok("Ok")
    }

    @Operation(summary = "Deletar vários registros", description = "Deletar vários registros")
    @DeleteMapping(
        TABLES_URL + "/lista/",
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
    fun delete(@PathVariable table: String, @RequestBody delete: List<ID>): ResponseEntity<String> {
        service.validTable(table)
        service.delete(table, delete)
        return ResponseEntity.ok("Ok")
    }
}