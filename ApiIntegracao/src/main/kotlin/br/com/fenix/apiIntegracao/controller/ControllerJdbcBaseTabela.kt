package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.controller.Endpoints.Companion.ATUALIZACAO_URL
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TABLES_URL
import br.com.fenix.apiintegracao.converters.MediaTypes
import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.mapper.Mapper
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import br.com.fenix.apiintegracao.service.ServiceJdbcTabela
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

abstract class ControllerJdbcBaseTabela<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcBaseTabela<ID, E, D, C>>(
    repository: RepositoryJdbcTabela<E, ID>,
    factory: EntityFactory<ID, E>
) : ControllerJdbcBase<ID, E, D, C>(repository, factory), ControllerJdbcTabela<ID, E, D, C> {
    protected val service: ServiceJdbcTabela<ID, E, D, C>
    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>
    private val clazzController: Class<C>

    abstract override fun getMapper(): ModelMapper

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
        clazzController = superclass.actualTypeArguments[3] as Class<C>
        service = object : ServiceJdbcTabela<ID, E, D, C>(repository, factory, clazzEntity, clazzDto, clazzController) {
            override val mapper: Mapper
                get() = Mapper(getMapper())
        }
    }

    @Operation(
        summary = "Tabelas disponíveis para a consulta.",
        description = """Retorna uma lista com os nomes de todas as tabelas disponíveis para consulta neste recurso.

**Corpo da Resposta:**
* Apresenta um array de strings, onde cada string é o nome de uma tabela válida que pode ser utilizada no parâmetro `{table}` dos outros endpoints."""
    )
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
    fun getTables(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(service.getTables())
    }

    @Operation(
        summary = "Pesquisa paginada",
        description = """Este endpoint realiza uma busca paginada pelos registros da tabela especificada.
                        
**Parâmetros de Path:**
* **`table`**: O nome da tabela onde a busca será realizada.
                        
**Parâmetros de Consulta:**
* **`page`** (opcional, padrão: `0`): O número da página a ser retornada (iniciando em zero).
* **`size`** (opcional, padrão: `1000`): A quantidade de registros por página.
* **`direction`** (opcional, padrão: `asc`): A ordem da classificação por ID (`asc` para ascendente, `desc` para descendente).
                        
**Corpo da Resposta:**
* Retorna um objeto de paginação (`PagedModel`) contendo a lista de registros completos para a página solicitada, além de informações de navegação (links HATEOAS) como primeira, última, próxima e página anterior."""
    )
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
    override fun getPage(
        @PathVariable table: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<D>
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        service.validTable(table)
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getPage(table, pageable, assembler))
    }

    @Operation(
        summary = "Pesquisa paginada apartir da data informada",
        description = """Realiza uma busca paginada, retornando apenas os registros da tabela especificada que foram criados ou atualizados a partir da data e hora informadas.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde a busca será realizada.
* **`updateDate`**: A data e hora de referência no formato ISO 8601 (ex: `2025-09-23T15:30:00`). Somente registros com data de atualização igual ou posterior serão retornados.
                        
**Parâmetros de Consulta:**
* **`page`** (opcional, padrão: `0`): O número da página.
* **`size`** (opcional, padrão: `1000`): A quantidade de registros por página.
* **`direction`** (opcional, padrão: `asc`): A ordem da classificação.
                        
**Corpo da Resposta:**
* Retorna um objeto de paginação (`PagedModel`) com a lista de registros filtrados e informações de navegação."""
    )
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
    override fun getLastSyncPage(
        @PathVariable table: String, @PathVariable updateDate: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "1000") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<D>
    ): ResponseEntity<PagedModel<EntityModel<D>>> {
        service.validTable(table)
        val sort = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(sort, "id"))
        return ResponseEntity.ok(service.getLastSyncPage(table, updateDate, pageable, assembler))
    }

    @Operation(
        summary = "Pesquisa por id",
        description = """Busca e retorna um único registro específico da tabela informada com base no seu identificador único (ID).

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde a busca será realizada.
* **`id`**: O ID do registro a ser recuperado.
                        
**Corpo da Resposta:**
* Retorna o objeto completo do registro solicitado.
* Caso o ID não seja encontrado, retorna um erro `404 Not Found`."""
    )
    @GetMapping(
        "$TABLES_URL/{id}",
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
    override fun getOne(@PathVariable table: String, @PathVariable id: ID): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service[table, id])
    }

    @Operation(
        summary = "Pesquisar todos",
        description = """Retorna uma lista completa com **todos** os registros da tabela especificada, sem paginação.

**Parâmetros de Path:**
* **`table`**: O nome da tabela a ser consultada.
                        
**Atenção:**
* O uso deste endpoint em tabelas com grande volume de dados pode impactar a performance. Prefira as buscas paginadas sempre que possível.
                        
**Corpo da Resposta:**
* Retorna um array JSON contendo todos os objetos do recurso na tabela."""
    )
    @GetMapping(
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
    fun getAll(@PathVariable table: String): ResponseEntity<List<D>> {
        service.validTable(table)
        return ResponseEntity.ok(service.getAll(table))
    }

    @Operation(
        summary = "Pesquisar todos apartir da data informada",
        description = """Retorna uma lista completa com **todos** os registros da tabela especificada que foram criados ou atualizados a partir da data e hora informadas, sem paginação.

**Parâmetros de Path:**
* **`table`**: O nome da tabela a ser consultada.
* **`updateDate`**: A data e hora de referência no formato ISO 8601 (ex: `2025-09-23T15:30:00`).
                        
**Atenção:**
* Pode retornar um grande volume de dados dependendo da data informada.
                        
**Corpo da Resposta:**
* Retorna um array JSON com todos os registros que atendem ao critério de data."""
    )
    @GetMapping(
        "$TABLES_URL/lista$ATUALIZACAO_URL",
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
        return ResponseEntity.ok(service.getAll(table, Utils.updateDateToLocalDateTime(updateDate)))
    }

    @Operation(
        summary = "Atualizar registro",
        description = """Atualiza um único registro existente na tabela especificada, identificado pelo ID fornecido na URL. O método PUT realiza uma substituição completa do recurso.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde o registro reside.
* **`id`**: O identificador único do registro que será atualizado.
                        
**Corpo da Requisição:**
* Aceita um objeto JSON completo que representa o **novo estado integral** do recurso.
* O `ID` dentro do corpo da requisição, se presente, deve ser consistente com o `id` fornecido na URL.
                        
**Corpo da Resposta:**
* Retorna o objeto completo do registro após a atualização bem-sucedida (status `200 OK`).
* Retorna um erro `404 Not Found` caso o `id` não corresponda a um registro existente na tabela especificada."""
    )
    @JsonView(Views.Summary::class)
    @PutMapping(
        "$TABLES_URL/{id}",
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
    fun update(@PathVariable table: String, @PathVariable id: ID, @RequestBody update: D): ResponseEntity<D> {
        service.validTable(table)
        return ResponseEntity.ok(service.update(table, id, update))
    }

    @Operation(
        summary = "Atualizar vários registros",
        description = """Atualiza múltiplos registros em uma única operação em lote na tabela especificada. O método PUT substitui integralmente cada registro correspondente.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde os registros serão atualizados.
                        
**Corpo da Requisição:**
* Aceita uma lista de objetos JSON completos. O `ID` dentro de cada objeto é usado para identificar o registro a ser atualizado.
                        
**Corpo da Resposta:**
* Para fins de eficiência, a resposta **retorna apenas uma lista com os IDs** dos registros que foram criados com sucesso.
* A ordem dos IDs na lista de resposta corresponde à ordem dos objetos enviados na requisição.""",
        operationId = "updateListBaseTabela"
    )
    @JsonView(Views.Summary::class)
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

    @Operation(
        summary = "Inserir registro",
        description = """Este endpoint permite a criação de um único registro na tabela especificada.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde o registro será inserido.
                        
**Corpo da Requisição:**
* Aceita um objeto completo para inserção. O `ID` geralmente pode ser omitido, pois será gerado pelo servidor.
                        
**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas o objeto com o ID** do registro que foi criado com sucesso."""
    )
    @JsonView(Views.Summary::class)
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
    fun create(@PathVariable table: String, @RequestBody create: D): ResponseEntity<D> {
        return ResponseEntity.ok(service.create(table, create))
    }

    @Operation(
        summary = "Inserir registro",
        description = """Este endpoint permite a criação de múltiplos registros em uma única chamada (operação em lote) na tabela especificada.

**Parâmetros de Path:**
* **`table`**: O nome da tabela onde os registros serão inseridos.
                        
**Corpo da Requisição:**
* Aceita uma lista de objetos completos para inserção.
                        
**Corpo da Resposta (Importante):**
* Para fins de eficiência, a resposta **retorna apenas uma lista com os IDs** dos registros que foram criados com sucesso.
* A ordem dos IDs na lista de resposta corresponde à ordem dos objetos enviados na requisição.""",
        operationId = "saveListBaseTabela"
    )
    @JsonView(Views.Summary::class)
    @PostMapping(
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
    fun create(@PathVariable table: String, @RequestBody create: List<D>): ResponseEntity<List<D>> {
        return ResponseEntity.ok(service.create(table, create))
    }

    @Operation(
        summary = "Deletar registro por id",
        description = """Deleta um único registro específico da tabela informada com base no seu identificador único (ID) informado na URL.

**Parâmetros de Path:**
* **`table`**: O nome da tabela de onde o registro será deletado.
* **`id`**: O ID do registro a ser deletado.
                        
**Corpo da Resposta:**
* Retorna uma mensagem de confirmação (ex: "Ok") ou um status `204 No Content` em caso de sucesso."""
    )
    @DeleteMapping(
        "$TABLES_URL/{id}",
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

    @Operation(
        summary = "Deletar vários registros",
        description = """Deleta múltiplos registros em uma única operação em lote na tabela especificada, com base em uma lista de IDs.

**Parâmetros de Path:**
* **`table`**: O nome da tabela de onde os registros serão deletados.
                        
**Corpo da Requisição:**
* Aceita um array JSON contendo os `IDs` dos registros a serem deletados.
                        
**Corpo da Resposta:**
* Retorna uma mensagem de confirmação (ex: "Ok") ou um status `204 No Content` em caso de sucesso.""",
        operationId = "deleteListIdBaseTabela"
    )
    @DeleteMapping(
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
    fun delete(@PathVariable table: String, @RequestBody delete: List<ID>): ResponseEntity<String> {
        service.validTable(table)
        service.delete(table, delete)
        return ResponseEntity.ok("Ok")
    }
}