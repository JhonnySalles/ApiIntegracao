package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TABLES_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemSmall
import br.com.fenix.apiintegracao.service.ServiceJdbcItemSmall
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType

abstract class ControllerJdbcBaseItemSmall<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseItemSmall<ID, E, D, C>>(repository: RepositoryJdbcItemSmall<E, ID>, factory: EntityFactory<ID, E>) {
    protected val service: ServiceJdbcItemSmall<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    abstract fun getMapper() : ModelMapper

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        service = object : ServiceJdbcItemSmall<ID, E, D, C>(repository, factory, clazzEntity, clazzDto, clazzController) {
            override val mapper: Mapper
                get() = Mapper(getMapper())
        }
    }

    @Operation(summary = "Pesquisar todos", description = "Pesquisar todos")
    @GetMapping(
        "$TABLES_URL/{idParent}/lista",
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
    fun getAll(@PathVariable table: String, @PathVariable idParent : ID): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.getAll(table, idParent))
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
    fun update(@PathVariable table: String, @RequestBody update: D): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, update))
    }

    @Operation(summary = "Atualizar vários registros", description = "Atualizar vários registros")
    @PutMapping(
        "$TABLES_URL/lista",
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
        "$TABLES_URL/{idParent}",
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
    fun create(@PathVariable table: String, @PathVariable idParent: ID, @RequestBody create: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(table, idParent, create))
    }

    @Operation(summary = "Inserir vários registros", description = "Inserir vários registros")
    @PostMapping(
        "$TABLES_URL/{idParent}/lista",
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
    fun create(@PathVariable table: String, @PathVariable idParent: ID, @RequestBody create: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.create(table, idParent, create))
    }

}