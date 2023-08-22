package br.com.fenix.apiIntegracao.controller.withjson

import br.com.fenix.apiIntegracao.TestConfigs
import br.com.fenix.apiIntegracao.dto.AccountCredentialDto
import br.com.fenix.apiIntegracao.dto.TokenDto
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.dto.wrapper.WrapperEstatisticaDto
import br.com.fenix.apiIntegracao.mapper.mock.MockEstatistica
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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class EstatisticaControllerTest {

    private lateinit var accessToken: String
    private lateinit var specObjeto: RequestSpecification
    private lateinit var specLista: RequestSpecification
    private lateinit var objectMapper: ObjectMapper

    private lateinit var objeto: EstatisticaDto
    private lateinit var lista: List<EstatisticaDto>

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objeto = MockEstatistica().mockDto()
        lista = MockEstatistica().mockDtoList()
    }

    private fun asserts(older: EstatisticaDto, new: EstatisticaDto) {
        Assert.assertNotNull(new)
        Assert.assertNotNull(new.getId())
        Assert.assertNotNull(new.kanji)
        Assert.assertNotNull(new.leitura)
        Assertions.assertTrue(new.getId() != null)
        assertEquals(older.kanji, new.kanji)
        assertEquals(older.leitura, new.leitura)
    }

    @Test
    @Order(0)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun authorization() {
        val user = AccountCredentialDto("admin", "admin")
        accessToken = RestAssured.given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(user)
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
            .setBasePath("/api/estatistica")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        specLista = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath("/api/estatistica/lista")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(objeto)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val persisted = objectMapper.readValue(content, EstatisticaDto::class.java)
        asserts(objeto, persisted)
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {
        objeto.leitura = "troca de leitura"
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(objeto)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, EstatisticaDto::class.java)
        asserts(objeto, persisted)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", objeto.getId())
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, EstatisticaDto::class.java)
        asserts(objeto, persisted)
    }

    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testDelete() {
        RestAssured.given().spec(specObjeto)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", objeto.getId())
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(5)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreateAll() {
        val content: String = RestAssured.given().spec(specLista)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(lista)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, List::class.java) as List<EstatisticaDto>
        Assert.assertNotNull(persisted)
        asserts(lista[0], persisted[0])
        asserts(lista[lista.size - 1], persisted[persisted.size - 1])
    }

    @Test
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdateAll() {
        lista[0].leitura = "Mudança de leitura"
        val content: String = RestAssured.given().spec(specLista)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(lista)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val persisted = objectMapper.readValue(content, List::class.java) as List<EstatisticaDto>
        Assert.assertNotNull(persisted)
        asserts(lista[0], persisted[0])
        asserts(lista[lista.size - 1], persisted[persisted.size - 1])
    }

    @Test
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val content: String = RestAssured.given().spec(specObjeto)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .queryParams("page", 0, "size", 5, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

        val wrapper = objectMapper.readValue(content, WrapperEstatisticaDto::class.java)
        val persisted = wrapper.embedded.estatisticas

        Assert.assertNotNull(persisted)
        asserts(lista[0], persisted[0])
        asserts(lista[lista.size - 1], persisted[persisted.size - 1])

    }

}