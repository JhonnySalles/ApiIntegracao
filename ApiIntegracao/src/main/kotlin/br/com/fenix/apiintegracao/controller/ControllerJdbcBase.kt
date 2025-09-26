package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbc
import br.com.fenix.apiintegracao.service.ServiceJdbcBase
import br.com.fenix.apiintegracao.utils.Utils
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType

abstract class ControllerJdbcBase<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBase<ID, E, D, C>>(
    repository: RepositoryJdbc<E, ID>,
    factory: EntityFactory<ID, E>
) : ControllerJdbc<ID, E, D, C> {
    private val service: ServiceJdbcBase<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    abstract fun getMapper(): ModelMapper

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        service = object : ServiceJdbcBase<ID, E, D, C>(repository, factory, clazzEntity, clazzDto, clazzController) {
            override val mapper: Mapper
                get() = Mapper(getMapper())
        }
    }

    @Operation(
        summary = "Pesquisa paginada",
        description = """Este endpoint realiza uma busca paginada pelos registros do recurso.

**Parâmetros de Consulta:**
* **`page`** (opcional, padrão: `0`): O número da página a ser retornada (iniciando em zero).
* **`size`** (opcional, padrão: `1000`): A quantidade de registros por página.
* **`direction`** (opcional, padrão: `asc`): A ordem da classificação por ID (`asc` para ascendente, `desc` para descendente).
                        
**Corpo da Resposta:**
* Retorna um objeto de paginação (`PagedModel`) contendo a lista de registros completos para a página solicitada, além de informações de navegação (links HATEOAS) como primeira, última, próxima e página anterior."""
    )
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
    override fun getPage(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<D>
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getPage(pageable, assembler))
    }

    @Operation(
        summary = "Pesquisa paginada apartir da data informada",
        description = """Realiza uma busca paginada, retornando apenas os registros que foram criados ou atualizados a partir da data e hora informadas.

**Parâmetros de Path:**
* **`updateDate`**: A data e hora de referência no formato ISO 8601 (ex: `2025-09-24T07:01:37`). Somente registros com data de atualização igual ou posterior serão retornados.
                        
**Parâmetros de Consulta:**
* **`page`** (opcional, padrão: `0`): O número da página.
* **`size`** (opcional, padrão: `1000`): A quantidade de registros por página.
* **`direction`** (opcional, padrão: `asc`): A ordem da classificação.
                        
**Corpo da Resposta:**
* Retorna um objeto de paginação (`PagedModel`) com a lista de registros filtrados e informações de navegação."""
    )
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
    override fun getLastSyncPage(
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

    @Operation(
        summary = "Pesquisa por id",
        description = """Busca e retorna um único registro específico com base no seu identificador único (ID).

**Parâmetros de Path:**
* **`id`**: O ID do registro a ser recuperado.
                        
**Corpo da Resposta:**
* Retorna o objeto completo do registro solicitado.
* Caso o ID não seja encontrado, retorna um erro `404 Not Found`."""
    )
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
    override fun getOne(@PathVariable id: ID): ResponseEntity<D> {
        return ResponseEntity.ok(service[id])
    }

    @Operation(
        summary = "Pesquisar todos",
        description = """Retorna uma lista completa com **todos** os registros do recurso, sem paginação.

**Atenção:**
* O uso deste endpoint em recursos com grande volume de dados pode impactar a performance. Prefira as buscas paginadas sempre que possível.
                        
**Corpo da Resposta:**
* Retorna um array JSON contendo todos os objetos do recurso."""
    )
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

    @Operation(
        summary = "Pesquisar todos apartir da data informada",
        description = """Retorna uma lista completa com **todos** os registros criados ou atualizados a partir da data e hora informadas, sem paginação.

**Parâmetros de Path:**
* **`updateDate`**: A data e hora de referência no formato ISO 8601 (ex: `2025-09-24T07:01:37`).
                        
**Atenção:**
* Pode retornar um grande volume de dados dependendo da data informada.
                        
**Corpo da Resposta:**
* Retorna um array JSON com todos os registros que atendem ao critério de data."""
    )
    @GetMapping(
        "/lista$ATUALIZACAO_URL",
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

    @Operation(
        summary = "Atualizar registro",
        description = """Atualiza um único registro existente, identificado pelo ID fornecido na URL. O método PUT realiza uma substituição completa do recurso.

**Parâmetros de Path:**
* **`id`**: O identificador único do registro que será atualizado.

**Corpo da Requisição:**
* Aceita um objeto JSON completo que representa o **novo estado integral** do recurso.
* O `ID` dentro do corpo da requisição, se presente, deve ser consistente com o `id` fornecido na URL.

**Corpo da Resposta:**
* Retorna o objeto completo do registro após a atualização bem-sucedida (status `200 OK`).
* Retorna um erro `404 Not Found` caso o `id` não corresponda a um registro existente."""
    )
    @JsonView(Views.Summary::class)
    @PutMapping(
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
    fun update(@PathVariable id: ID, @RequestBody update: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.update(id, update))
    }

    @Operation(
        summary = "Atualizar vários registros",
        description = """Atualiza múltiplos registros em uma única operação em lote. O método PUT substitui integralmente cada registro correspondente.

**Corpo da Requisição:**
* Aceita uma lista de objetos JSON completos. O `ID` dentro de cada objeto é usado para identificar o registro a ser atualizado.

**Corpo da Resposta:**
* Para fins de eficiência, a resposta **retorna apenas uma lista com os IDs** dos registros que foram criados com sucesso.
* A ordem dos IDs na lista de resposta corresponde à ordem dos objetos enviados na requisição.""",
        operationId = "updateListBase"
    )
    @JsonView(Views.Summary::class)
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

    @Operation(
        summary = "Inserir registro",
        description = """Este endpoint permite a criação de um único registro.
                        
**Corpo da Requisição:**
* Aceita um objeto completo para inserção. O `ID` geralmente pode ser omitido, pois será gerado pelo servidor.

**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas o objeto com o ID** do registro que foi criado com sucesso."""
    )
    @JsonView(Views.Summary::class)
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

    @Operation(
        summary = "Inserir vários registros",
        description = """Este endpoint permite a criação de múltiplos registros em uma única chamada (operação em lote).

**Corpo da Requisição:**
* Aceita uma lista de objetos completos para inserção.

**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas uma lista com os IDs** dos registros que foram criados com sucesso.
* A ordem dos IDs na lista de resposta corresponde à ordem dos objetos enviados na requisição.""",
        operationId = "saveListBase"
    )
    @JsonView(Views.Summary::class)
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

    @Operation(
        summary = "Deletar registro",
        description = """Deleta um único registro com base no objeto enviado no corpo da requisição.

**Corpo da Requisição:**
* Aceita um objeto JSON contendo o `ID` do registro a ser deletado.

**Corpo da Resposta:**
* Retorna uma mensagem de confirmação (ex: "Ok") ou um status `204 No Content` em caso de sucesso."""
    )
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
    fun delete(@RequestBody id: D): ResponseEntity<String> {
        service.delete(id)
        return ResponseEntity.ok("Ok")
    }

    @Operation(
        summary = "Deletar registro por id",
        description = """Deleta um único registro específico com base no seu identificador único (ID) informado na URL.

**Parâmetros de Path:**
* **`id`**: O ID do registro a ser deletado.

**Corpo da Resposta:**
* Retorna uma mensagem de confirmação (ex: "Ok") ou um status `204 No Content` em caso de sucesso."""
    )
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

    @Operation(
        summary = "Deletar vários registros",
        description = """Deleta múltiplos registros em uma única operação em lote, com base em uma lista de IDs.

**Corpo da Requisição:**
* Aceita um array JSON contendo os `IDs` dos registros a serem deletados.

**Corpo da Resposta:**
* Retorna uma mensagem de confirmação (ex: "Ok") ou um status `204 No Content` em caso de sucesso.""",
        operationId = "deleteListIdBase"
    )
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

    @Operation(
        summary = "Atualizar parcialmente o registro",
        description = """Atualiza parcialmente um único registro. Diferente do PUT, o PATCH modifica **apenas** os campos que são fornecidos no corpo da requisição, mantendo os demais inalterados.

**Corpo da Requisição:**
* Aceita um objeto JSON contendo o `ID` e somente os campos que devem ser alterados.

**Corpo da Resposta:**
* Retorna o objeto completo após a atualização parcial ter sido aplicada."""
    )
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

    @Operation(
        summary = "Atualizar parcialmente vários registros",
        description = """Atualiza parcialmente múltiplos registros em uma única operação em lote.

**Corpo da Requisição:**
* Aceita uma lista de objetos JSON. Cada objeto deve conter um `ID` e os campos a serem alterados para aquele registro específico.

**Corpo da Resposta:**
* Retorna uma lista contendo os objetos completos após a aplicação das atualizações parciais.""",
        operationId = "partialListBase"
    )
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