package br.com.fenix.apiIntegracao.mapper.mock

import br.com.fenix.apiIntegracao.dto.textojapones.FilaSqlDto
import br.com.fenix.apiIntegracao.model.textojapones.FilaSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*
import kotlin.random.Random

class MockFilaSql : Mock<UUID?, FilaSql, FilaSqlDto> {

    override fun mockEntity(): FilaSql {
        return mockEntity(UUID.fromString("f9b0e6e3-8b2d-4399-8a48-81c055a9d4d2"))
    }

    override fun mockDto(): FilaSqlDto {
        return mockDto(UUID.fromString("f9b0e6e3-8b2d-4399-8a48-81c055a9d4d2"))
    }

    override fun mockEntityList(): List<FilaSql> {
        val list: MutableList<FilaSql> = ArrayList<FilaSql>()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<FilaSqlDto> {
        val list: MutableList<FilaSqlDto> = ArrayList<FilaSqlDto>()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): FilaSql {
        return FilaSql(
            id, 0, "SELECT 1 > 0", "UPDATE", "DELETE", "VOCABULARIO",
            Random.nextBoolean(), Random.nextBoolean()
        )
    }

    override fun mockDto(id: UUID?): FilaSqlDto {
        return FilaSqlDto(
            id, 0, "SELECT 1 > 0", "UPDATE", "DELETE", "VOCABULARIO",
            Random.nextBoolean(), Random.nextBoolean()
        )
    }

    override fun assertsToDto(input: FilaSql, output: FilaSqlDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencial, output.sequencial)
        assertEquals(input.selectSQL, output.selectSQL)
        assertEquals(input.updateSQL, output.updateSQL)
        assertEquals(input.deleteSQL, output.deleteSQL)
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.isExporta, output.isExporta)
        assertEquals(input.isLimpeza, output.isLimpeza)
    }

    override fun assertsFromDto(input: FilaSqlDto, output: FilaSql) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencial, output.sequencial)
        assertEquals(input.selectSQL, output.selectSQL)
        assertEquals(input.updateSQL, output.updateSQL)
        assertEquals(input.deleteSQL, output.deleteSQL)
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.isExporta, output.isExporta)
        assertEquals(input.isLimpeza, output.isLimpeza)
    }
    override fun assertsService(input: FilaSqlDto?) {
        assertNotNull(input)
        assertNotNull(input!!.selectSQL)
        assertNotNull(input.vocabulario)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.sequencial, aux.sequencial)
        assertEquals(input.selectSQL, aux.selectSQL)
        assertEquals(input.updateSQL, aux.updateSQL)
        assertEquals(input.deleteSQL, aux.deleteSQL)
        assertEquals(input.vocabulario, aux.vocabulario)
        assertEquals(input.isExporta, aux.isExporta)
        assertEquals(input.isLimpeza, aux.isLimpeza)
    }
}