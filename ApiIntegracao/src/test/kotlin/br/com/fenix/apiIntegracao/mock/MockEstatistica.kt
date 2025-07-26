package br.com.fenix.apiIntegracao.mock

import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.model.textojapones.EstatisticaJapones
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*

class MockEstatistica : Mock<UUID?, EstatisticaJapones, EstatisticaDto> {

    override fun mockEntity(): EstatisticaJapones {
        return mockEntity(UUID.fromString("6cd56de7-0f4d-4f18-ab0d-2bd3e4fc40bd"))
    }

    override fun mockDto(): EstatisticaDto {
        return mockDto(UUID.fromString("6cd56de7-0f4d-4f18-ab0d-2bd3e4fc40bd"))
    }

    override fun mockEntityList(): List<EstatisticaJapones> {
        val list: MutableList<EstatisticaJapones> = ArrayList<EstatisticaJapones>()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<EstatisticaDto> {
        val list: MutableList<EstatisticaDto> = ArrayList<EstatisticaDto>()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): EstatisticaJapones {
        return EstatisticaJapones(
            id,
            1,
            "猫",
            "ねこ",
            "On",
            100.0,
            3.5f,
            15.0,
            1.0f,
            0
        )
    }

    override fun mockDto(id: UUID?): EstatisticaDto {
        return EstatisticaDto(
            id,
            1,
            "猫",
            "ねこ",
            "On",
            100.0,
            3.5f,
            15.0,
            1.0f,
            0
        )
    }

    override fun assertsToDto(input: EstatisticaJapones, output: EstatisticaDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencial, output.sequencial)
        assertEquals(input.kanji, output.kanji)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.tipo, output.tipo)
        assertEquals(input.quantidade, output.quantidade)
        assertEquals(input.percentual, output.percentual)
        assertEquals(input.media, output.media)
        assertEquals(input.percentualMedio, output.percentualMedio)
        assertEquals(input.corSequencial, output.corSequencial)
    }

    override fun assertsFromDto(input: EstatisticaDto, output: EstatisticaJapones) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencial, output.sequencial)
        assertEquals(input.kanji, output.kanji)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.tipo, output.tipo)
        assertEquals(input.quantidade, output.quantidade)
        assertEquals(input.percentual, output.percentual)
        assertEquals(input.media, output.media)
        assertEquals(input.percentualMedio, output.percentualMedio)
        assertEquals(input.corSequencial, output.corSequencial)
    }

    override fun assertsService(input: EstatisticaDto?) {
        assertNotNull(input)
        assertNotNull(input!!.kanji)
        assertNotNull(input.quantidade)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.sequencial, aux.sequencial)
        assertEquals(input.kanji, aux.kanji)
        assertEquals(input.leitura, aux.leitura)
        assertEquals(input.tipo, aux.tipo)
        assertEquals(input.quantidade, aux.quantidade)
        assertEquals(input.percentual, aux.percentual)
        assertEquals(input.media, aux.media)
        assertEquals(input.percentualMedio, aux.percentualMedio)
        assertEquals(input.corSequencial, aux.corSequencial)
    }

}