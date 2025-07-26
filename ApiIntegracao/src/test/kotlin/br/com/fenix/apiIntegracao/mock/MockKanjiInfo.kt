package br.com.fenix.apiIntegracao.mock

import br.com.fenix.apiIntegracao.dto.textojapones.KanjiInfoDto
import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*
import kotlin.random.Random

class MockKanjiInfo : Mock<UUID?, KanjiInfo, KanjiInfoDto> {

    override fun mockEntity(): KanjiInfo {
        return mockEntity(UUID.fromString("9e0d941e-7510-4fae-8e8f-cdc96be86bde"))
    }

    override fun mockDto(): KanjiInfoDto {
        return mockDto(UUID.fromString("9e0d941e-7510-4fae-8e8f-cdc96be86bde"))
    }

    override fun mockEntityList(): List<KanjiInfo> {
        val list: MutableList<KanjiInfo> = ArrayList<KanjiInfo>()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<KanjiInfoDto> {
        val list: MutableList<KanjiInfoDto> = ArrayList<KanjiInfoDto>()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): KanjiInfo {
        return KanjiInfo(
            id, Random.nextLong(), "猫", "ねこ", Random.nextInt(5), "KANJI"
        )
    }

    override fun mockDto(id: UUID?): KanjiInfoDto {
        return KanjiInfoDto(
            id, Random.nextLong(), "猫", "ねこ", Random.nextInt(5), "KANJI"
        )
    }

    override fun assertsToDto(input: KanjiInfo, output: KanjiInfoDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencia, output.sequencia)
        assertEquals(input.word, output.word)
        assertEquals(input.readInfo, output.readInfo)
        assertEquals(input.frequency, output.frequency)
        assertEquals(input.tabela, output.tabela)
    }

    override fun assertsFromDto(input: KanjiInfoDto, output: KanjiInfo) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.sequencia, output.sequencia)
        assertEquals(input.word, output.word)
        assertEquals(input.readInfo, output.readInfo)
        assertEquals(input.frequency, output.frequency)
        assertEquals(input.tabela, output.tabela)
    }

    override fun assertsService(input: KanjiInfoDto?) {
        assertNotNull(input)
        assertNotNull(input!!.word)
        assertNotNull(input.frequency)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.sequencia, aux.sequencia)
        assertEquals(input.word, aux.word)
        assertEquals(input.readInfo, aux.readInfo)
        assertEquals(input.frequency, aux.frequency)
        assertEquals(input.tabela, aux.tabela)
    }
}