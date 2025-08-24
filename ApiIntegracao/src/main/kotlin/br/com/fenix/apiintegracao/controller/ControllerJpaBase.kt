package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJpaBase
import br.com.fenix.apiintegracao.service.ServiceJpaBase
import br.com.fenix.apiintegracao.utils.Utils
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.reflect.ParameterizedType
import java.time.LocalDateTime

abstract class ControllerJpaBase<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJpaBase<ID, E, D, C, R>, R : RepositoryJpaBase<E, ID>>(factory: EntityFactory<ID, E>) {
    private val service: ServiceJpaBase<ID, E, D, C, R>

    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>
    private val clazzRepository: Class<R>

    abstract fun getMapper() : ModelMapper
    abstract fun getDynamicRegistry() : DynamicJpaRepositoryRegistry
    abstract val conexao : Conexao

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        clazzRepository = superclass.actualTypeArguments[4] as Class<R>
        service = object : ServiceJpaBase<ID, E, D, C, R>(factory, clazzEntity, clazzDto, clazzController) {
            override val repository: RepositoryJpaBase<E, ID>
                get() = getDynamicRegistry().getRepository(conexao, clazzRepository) ?: throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço indisponível no momento.")
            override val mapper: Mapper
                get() = Mapper(getMapper())
        }
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
    fun getPage(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<D>
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getPage(pageable, assembler))
    }

    @Operation(summary = "Pesquisa paginada apartir da data informada", description = "Pesquisa paginada apartir da data informada")
    @GetMapping(
        ATUALIZACAO_URL,
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
        @PathVariable updateDate: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<D>
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getLastSyncPage(updateDate, pageable, assembler))
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
        "/lista",
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
        "/lista/$ATUALIZACAO_URL",
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
    fun getAllLastSync(@PathVariable updateDate: String): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.getAll(Utils.updateDateToLocalDateTime(updateDate)))
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
    fun update(@RequestBody update: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.update(update))
    }

    @Operation(summary = "Atualizar vários registros", description = "Atualizar vários registros")
    @PutMapping(
        "/lista",
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
    fun create(@RequestBody create: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(create))
    }

    @Operation(summary = "Inserir vários registros", description = "Inserir vários registros")
    @PostMapping(
        "/lista",
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
    fun delete(@PathVariable id: D): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }

    @Operation(summary = "Deletar registro por id", description = "Deletar registro por id")
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
        "/lista",
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

    @Operation(summary = "Atualizar parcialmente o registro", description = "Atualizar parcialmente o registro")
    @PatchMapping(
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
    fun patch(@RequestBody update: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.patch(update))
    }

    @Operation(summary = "Atualizar parcialmente vários registros", description = "Atualizar parcialmente vários registros")
    @PatchMapping(
        "/lista",
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
    fun patch(@RequestBody update: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.patch(update))
    }
}