package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.TestConfigs
import br.com.fenix.apiIntegracao.dto.DtoBase
import br.com.fenix.apiIntegracao.dto.api.TokenDto
import br.com.fenix.apiIntegracao.dto.wrapper.WrapperDtoBase
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.model.Entity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.Assert
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
abstract class BaseControllerTest<ID, E : Entity<E, ID>, D : DtoBase<ID>, W : WrapperDtoBase<D>>(
    var clazzDto: Class<D>,
    var clazzWrapper: Class<W>,
    var mock: Mock<ID, E, D>,
    var contentType: String
) {

    abstract var testName: String

    abstract var pathEndpointObject: String
    abstract var pathEndpointList: String

    abstract var pathEndpointPaginadoAtualizacao: String
    abstract var pathEndpointListaAtualizacao: String

    private lateinit var accessToken: String
    private lateinit var specObjeto: RequestSpecification
    private lateinit var specLista: RequestSpecification
    private lateinit var objectMapper: ObjectMapper

    private lateinit var objeto: D
    private lateinit var lista: List<D>

    private lateinit var atualizacao : LocalDateTime

    @BeforeAll
    fun setup() {
        println("Start test controller for -- $testName --")
        atualizacao = LocalDateTime.now()
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objeto = mock.mockDto()
        lista = mock.mockDtoList()
    }

    abstract fun asserts(older: D, new: D)
    abstract fun updateObject(objeto: D): D

    @Test
    @DisplayName("Autorization on api")
    @Order(0)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun authorization() {
        accessToken = RestAssured.given()
            .basePath(TestConfigs.ENDPOINT_LOGIN)
            .port(TestConfigs.SERVER_PORT)
            .contentType(contentType)
            .accept(contentType)
            .body(TestConfigs.getCredential())
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenDto::class.java)
            .accessToken

        specObjeto = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath(pathEndpointObject)
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        specLista = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath(pathEndpointList)
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @DisplayName("Testing enpoint create object.")
    @Order(1)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(contentType)
            .accept(contentType)
            .body(objeto)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val persisted = objectMapper.readValue(content, clazzDto)
        asserts(objeto, persisted)
    }

    @Test
    @DisplayName("Testing enpoint update object.")
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {
        objeto = updateObject(objeto)
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(contentType)
            .accept(contentType)
            .body(objeto)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, clazzDto)
        asserts(objeto, persisted)
    }

    @Test
    @DisplayName("Testing enpoint find object by id.")
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(contentType)
            .accept(contentType)
            .pathParam("id", objeto.getId())
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, clazzDto)
        asserts(objeto, persisted)
    }

    @Test
    @DisplayName("Testing enpoint delete object.")
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testDelete() {
        RestAssured.given().spec(specObjeto)
            .contentType(contentType)
            .accept(contentType)
            .pathParam("id", objeto.getId())
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @DisplayName("Testing enpoint create list object.")
    @Order(5)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreateAll() {
        val content: String = RestAssured.given().spec(specLista)
            .contentType(contentType)
            .accept(contentType)
            .body(lista)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, List::class.java) as List<D>
        Assert.assertNotNull(persisted)
        Assert.assertEquals(lista.size, persisted.size)
        asserts(lista[0], persisted[0])
        asserts(lista[lista.size - 1], persisted[persisted.size - 1])
    }

    @Test
    @DisplayName("Testing enpoint update list object.")
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdateAll() {
        updateObject(lista[0])
        val content: String = RestAssured.given().spec(specLista)
            .contentType(contentType)
            .accept(contentType)
            .body(lista)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, List::class.java) as List<D>
        Assert.assertNotNull(persisted)
        Assert.assertEquals(lista.size, persisted.size)
        asserts(lista[0], persisted[0])
        asserts(lista[lista.size - 1], persisted[persisted.size - 1])
    }

    @Test
    @DisplayName("Testing enpoint find list object paging.")
    @Order(7)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(contentType)
            .accept(contentType)
            .queryParams("page", 0, "size", 5, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

        val wrapper = objectMapper.readValue(content, clazzWrapper)
        val persisted = wrapper.embedded.getList()

        Assert.assertNotNull(persisted)
        asserts(lista[0], persisted[0])
        asserts(lista[5], persisted[5])
    }

    @Test
    @DisplayName("Testing enpoint find list object.")
    @Order(8)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAllList() {
        val content: String = RestAssured.given().spec(specLista)
            .contentType(contentType)
            .accept(contentType)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

        val persisted = objectMapper.readValue(content, List::class.java) as List<D>

        Assert.assertNotNull(persisted)
        asserts(lista[0], persisted[0])
        asserts(lista[5], persisted[5])
    }

    @Test
    @DisplayName("Testing enpoint find list object after update date.")
    @Order(9)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testAtualizacaoPaginado() {
        val spec = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath(pathEndpointPaginadoAtualizacao)
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content: String = RestAssured.given().spec(spec)
            .contentType(contentType)
            .accept(contentType)
            .queryParams("page", 0, "size", 5, "direction", "asc")
            .pathParam("atualizacao", atualizacao.toString())
            .`when`()
            .get("atualizacao")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

        val wrapper = objectMapper.readValue(content, clazzWrapper)
        val persisted = wrapper.embedded.getList()

        Assert.assertNotNull(persisted)
        Assert.assertTrue(persisted.isNotEmpty())
    }

    @Test
    @DisplayName("Testing enpoint find list object after update date.")
    @Order(10)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testAtualizacaoLista() {
        val spec = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath(pathEndpointListaAtualizacao)
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content: String = RestAssured.given().spec(spec)
            .contentType(contentType)
            .accept(contentType)
            .pathParam("atualizacao", atualizacao.toString())
            .`when`()
            .get("atualizacao")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

        val persisted = objectMapper.readValue(content, List::class.java) as List<D>

        Assert.assertNotNull(persisted)
        Assert.assertTrue(persisted.isNotEmpty())
    }

    @Test
    @DisplayName("Testing enpoint delete list object.")
    @Order(11)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testDeleteAll() {
        var deletar = lista.stream().map { it.getId() }.toList()
        RestAssured.given().spec(specLista)
            .contentType(contentType)
            .accept(contentType)
            .body(deletar)
            .`when`()
            .delete()
            .then()
            .statusCode(204)
    }

}