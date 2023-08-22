package br.com.fenix.apiIntegracao.controller.cors

import br.com.fenix.apiIntegracao.TestConfigs
import br.com.fenix.apiIntegracao.dto.AccountCredentialDto
import br.com.fenix.apiIntegracao.dto.TokenDto
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
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

class EstatisticaCorsControllerTest {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper

    private lateinit var estatistica: EstatisticaDto

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        estatistica = MockEstatistica().mockDto()
    }

    @Test
    @Order(0)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun authorization() {
        val user = AccountCredentialDto("admin", "admin")
        val accessToken = RestAssured.given()
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

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $accessToken")
            .setBasePath("/api/estatistica")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        val content: String = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(estatistica)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val persisted = objectMapper.readValue(content, EstatisticaDto::class.java)
        Assert.assertNotNull(persisted)
        Assert.assertNotNull(persisted.getId())
        Assert.assertNotNull(persisted.kanji)
        Assert.assertNotNull(persisted.leitura)
        Assertions.assertTrue(persisted.getId() != null)
        assertEquals(estatistica.kanji, persisted.kanji)
        assertEquals(estatistica.leitura, persisted.leitura)
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreateWithWrongOrigin() {
        val content: String = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(estatistica)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
        Assert.assertNotNull(content)
        Assertions.assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", estatistica.getId())
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val persisted = objectMapper.readValue(content, EstatisticaDto::class.java)
        Assert.assertNotNull(persisted)
        Assert.assertNotNull(persisted.getId())
        Assert.assertNotNull(persisted.kanji)
        Assert.assertNotNull(persisted.leitura)
        Assertions.assertTrue(persisted.getId() != null)
        assertEquals(estatistica.kanji, persisted.kanji)
        assertEquals(estatistica.leitura, persisted.leitura)
    }


    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindByIdWithWrongOrigin() {
        val content: String = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", estatistica.getId())
            .`when`()
            .get("{id}")
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
        Assert.assertNotNull(content)
        assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(5)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testDelete() {
        RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", estatistica.getId())
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

}