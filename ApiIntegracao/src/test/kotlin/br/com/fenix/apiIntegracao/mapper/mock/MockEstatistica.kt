package br.com.fenix.apiIntegracao.mapper.mock

import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*

class MockEstatistica : Mock<UUID, Estatistica, EstatisticaDto> {

    override fun mockEntity(): Estatistica {
        return mockEntity(UUID.fromString("0"))
    }

    override fun mockDto(): EstatisticaDto {
        return mockDto(UUID.fromString("0"))
    }

    override fun mockEntityList(): List<Estatistica> {
        val list: MutableList<Estatistica> = ArrayList<Estatistica>()
        for (i in 0..13)
            list.add(mockEntity(UUID.fromString("$i")))
        return list
    }

    override fun mockDtoList(): List<EstatisticaDto> {
        val list: MutableList<EstatisticaDto> = ArrayList<EstatisticaDto>()
        for (i in 0..13)
            list.add(mockDto(UUID.fromString("$i")))
        return list
    }

    override fun mockEntity(id: UUID): Estatistica {
        return Estatistica(
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

    override fun mockDto(id: UUID): EstatisticaDto {
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

    override fun assertsToDto(input: Estatistica, output: EstatisticaDto) {
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

    override fun assertsFromDto(input: EstatisticaDto, output: Estatistica) {
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