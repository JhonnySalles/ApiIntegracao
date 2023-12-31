package br.com.fenix.apiIntegracao.mapper.mock

import br.com.fenix.apiIntegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiIntegracao.model.textojapones.ExclusaoJapones
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

class MockExclusao : Mock<UUID?, ExclusaoJapones, ExclusaoDto> {

    override fun mockEntity(): ExclusaoJapones {
        return mockEntity(UUID.fromString("a3b75d19-aeb9-459d-85ed-5b2f5d003ff9"))
    }

    override fun mockDto(): ExclusaoDto {
        return mockDto(UUID.fromString("a3b75d19-aeb9-459d-85ed-5b2f5d003ff9"))
    }

    override fun mockEntityList(): List<ExclusaoJapones> {
        val list: MutableList<ExclusaoJapones> = ArrayList<ExclusaoJapones>()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<ExclusaoDto> {
        val list: MutableList<ExclusaoDto> = ArrayList<ExclusaoDto>()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): ExclusaoJapones {
        return ExclusaoJapones(
            id,
            "Teste exclusao"
        )
    }

    override fun mockDto(id: UUID?): ExclusaoDto {
        return ExclusaoDto(
            id,
            "Teste exclusao"
        )
    }

    override fun assertsToDto(input: ExclusaoJapones, output: ExclusaoDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.exclusao, output.exclusao)
    }

    override fun assertsFromDto(input: ExclusaoDto, output: ExclusaoJapones) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.exclusao, output.exclusao)
    }

    override fun assertsService(input: ExclusaoDto?) {
        Assertions.assertNotNull(input)
        Assertions.assertNotNull(input!!.exclusao)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.exclusao, aux.exclusao)
    }
}