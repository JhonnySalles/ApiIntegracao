package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TABLES_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemSmall
import br.com.fenix.apiintegracao.service.ServiceJdbcItemSmall
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType

abstract class ControllerJdbcBaseItemSmall<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseItemSmall<ID, E, D, C>>(
    repository: RepositoryJdbcItemSmall<E, ID>,
    factory: EntityFactory<ID, E>
) {
    protected val service: ServiceJdbcItemSmall<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    abstract fun getMapper(): ModelMapper

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

    @Operation(
        summary = "Pesquisar todos",
        description = """Busca e retorna uma lista completa com **todos** os registros-filho associados a um registro-pai específico.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde a busca será realizada.
* **`idParent`**: O identificador único do registro-pai ao qual os itens pertencem.
                        
**Atenção:**
* Este endpoint retorna todos os itens associados sem paginação, o que pode impactar a performance caso o registro-pai possua um grande número de filhos.
                        
**Corpo da Resposta:**
* Retorna um array JSON contendo os objetos completos de todos os registros-filho encontrados."""
    )
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
    fun getAll(@PathVariable table: String, @PathVariable idParent: ID): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.getAll(table, idParent))
    }

    @Operation(
        summary = "Atualizar registro",
        description = """Atualiza um único registro-filho existente, identificado por seu ID e pelo ID de seu pai na URL. O método PUT realiza uma substituição completa do recurso.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde o registro reside.
* **`idParent`**: O identificador único do registro-pai ao qual este item pertence.
* **`id`**: O identificador único do registro-filho que será atualizado.
                        
**Corpo da Requisição:**
* Aceita um objeto JSON completo que representa o **novo estado integral** do registro-filho.
* Os IDs (do próprio registro e do pai) dentro do corpo da requisição, se presentes, devem ser consistentes com os `id` e `idParent` fornecidos na URL.
                        
**Corpo da Resposta:**
* Retorna o objeto completo do registro-filho após a atualização bem-sucedida (status `200 OK`).
* Retorna um erro `404 Not Found` caso o `id` ou `idParent` não correspondam a um registro existente."""
    )
    @JsonView(Views.Summary::class)
    @PutMapping(
        "$TABLES_URL/{idParent}/{id}",
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
    fun update(@PathVariable table: String, @PathVariable idParent: ID, @PathVariable id: ID, @RequestBody update: D): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, idParent, id, update))
    }

    @Operation(
        summary = "Atualizar vários registros",
        description = """Atualiza múltiplos registros-filho de um mesmo pai em uma única operação em lote. O método PUT substitui integralmente cada registro correspondente.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde os registros residem.
* **`idParent`**: O identificador único do registro-pai ao qual todos os itens da lista pertencem.
                        
**Corpo da Requisição:**
* Aceita uma lista de objetos JSON completos. Cada objeto na lista deve conter seu próprio `ID` para ser identificado e atualizado.
                        
**Corpo da Resposta:**
* Retorna uma lista contendo os objetos-filho completos após a atualização, na mesma ordem em que foram enviados na requisição (status `200 OK`).""",
        operationId = "updateListBaseSmall"
    )
    @JsonView(Views.Summary::class)
    @PutMapping(
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
    fun update(@PathVariable table: String, @PathVariable idParent: ID, @RequestBody update: List<D>): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, idParent, update))
    }

    @Operation(
        summary = "Inserir registro",
        description = """Este endpoint permite a criação de um único registro-filho, associando-o diretamente a um registro-pai.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde o registro será inserido.
* **`idParent`**: O identificador único do registro-pai ao qual este novo item será associado.
                        
**Corpo da Requisição:**
* Aceita um objeto completo para inserção. O `ID` do item-filho geralmente pode ser omitido.
                        
**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas o objeto com o ID** do registro-filho que foi criado com sucesso."""
    )
    @JsonView(Views.Summary::class)
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

    @Operation(
        summary = "Inserir registro",
        description = """Este endpoint permite a criação de múltiplos registros-filho em uma única chamada (operação em lote), associando todos ao mesmo registro-pai.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde os registros serão inseridos.
* **`idParent`**: O identificador único do registro-pai ao qual todos os novos itens serão associados.
                        
**Corpo da Requisição:**
* Aceita uma lista de objetos completos para inserção.
                        
**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas uma lista com os IDs** dos registros-filho que foram criados com sucesso.
* A ordem dos IDs na lista de resposta corresponde à ordem dos objetos enviados na requisição.""",
        operationId = "saveListBaseSmall"
    )
    @JsonView(Views.Summary::class)
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