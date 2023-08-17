package br.com.fenix.apiIntegracao.repository

import br.com.fenix.apiIntegracao.controller.textojapones.EstatisticaController
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.mapper.mock.MockEstatistica
import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import br.com.fenix.apiIntegracao.repository.textojapones.EstatisticaRepository
import br.com.fenix.apiIntegracao.service.Service
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class EstatisticaRepositoryTest(@Mock var repository: EstatisticaRepository) {
    lateinit var input: MockEstatistica

    @InjectMocks
    var service = object : Service<UUID?, Estatistica, EstatisticaDto, EstatisticaController>(repository, Estatistica::class.java, EstatisticaDto::class.java, EstatisticaController::class.java) {}

    @BeforeEach
    @Throws(Exception::class)
    fun setUpMocks() {
        input = MockEstatistica()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindById() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        val result: EstatisticaDto = service.get(id)
        input.assertsService(result)
    }

    @Test
    fun testCreate() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        val persisted = entity
        val dto = input.mockDto(id)
        Mockito.`when`(repository.save(entity)).thenReturn(persisted)
        input.assertsService(service.create(dto))
    }

    @Test
    fun testCreateWithNullObject() {
        val exception: Exception = Assertions.assertThrows(RequiredObjectIsNullException::class.java) {
            service.create(null)
        }
        val expectedMessage = "Its required inform a object"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testUpdate() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        val persisted = entity
        val dto = input.mockDto(id)

        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        Mockito.`when`(repository.save(entity)).thenReturn(persisted)

        input.assertsService(service.update(dto))
    }


    @Test
    fun testUpdateWithNullObject() {
        val exception: Exception = Assertions.assertThrows(RequiredObjectIsNullException::class.java) {
            service.update(null)
        }
        val expectedMessage = "Its required inform a object"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testDelete() {
        val id = UUID.fromString("1")
        val entity = input.mockEntity(id)
        Mockito.`when`(repository.findById(id)).thenReturn(Optional.of(entity))
        service.delete(id)
    }

    @Test
    fun testFindAll() {
        val list: List<Estatistica> = input.mockEntityList()
        Mockito.`when`(repository.findAll()).thenReturn(list)
        val dtos = service.getAll()

        Assertions.assertNotNull(dtos)

        input.assertsToDto(list[0], dtos[0])
        input.assertsToDto(list[list.size / 2], dtos[dtos.size / 2])
        input.assertsToDto(list[list.size - 1], dtos[dtos.size - 1])
    }
}