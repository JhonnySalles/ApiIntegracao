package br.com.fenix.apiIntegracao.mapper.mock

import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.model.textojapones.Revisar
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*
import kotlin.random.Random

class MockRevisar : Mock<UUID, Revisar, RevisarDto> {

    override fun mockEntity(): Revisar {
        return mockEntity(UUID.fromString("0"))
    }

    override fun mockDto(): RevisarDto {
        return mockDto(UUID.fromString("0"))
    }

    override fun mockEntityList(): List<Revisar> {
        val list: MutableList<Revisar> = ArrayList()
        for (i in 0..13)
            list.add(mockEntity(UUID.fromString("$i")))
        return list
    }

    override fun mockDtoList(): List<RevisarDto> {
        val list: MutableList<RevisarDto> = ArrayList()
        for (i in 0..13)
            list.add(mockDto(UUID.fromString("$i")))
        return list
    }

    override fun mockEntity(id: UUID): Revisar {
        return Revisar(
            id,
            "猫",
            "猫",
            "ねこ",
            "gato",
            "cat",
            Random.nextBoolean(),
            Random.nextInt(),
            Random.nextBoolean(),
            Random.nextBoolean()
        )
    }

    override fun mockDto(id: UUID): RevisarDto {
        return RevisarDto(
            id,
            "猫",
            "猫",
            "ねこ",
            "gato",
            "cat",
            Random.nextBoolean(),
            Random.nextInt(),
            Random.nextBoolean(),
            Random.nextBoolean()
        )
    }

    override fun assertsToDto(input: Revisar, output: RevisarDto) {
        assertEquals(input.getId().toString(), output.id.toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.traducao, output.traducao)
        assertEquals(input.ingles, output.ingles)
        assertEquals(input.revisado, output.revisado)
        assertEquals(input.aparece, output.aparece)
        assertEquals(input.isAnime, output.isAnime)
        assertEquals(input.isManga, output.isManga)
    }

    override fun assertsFromDto(input: RevisarDto, output: Revisar) {
        assertEquals(input.id.toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.traducao, output.traducao)
        assertEquals(input.ingles, output.ingles)
        assertEquals(input.revisado, output.revisado)
        assertEquals(input.aparece, output.aparece)
        assertEquals(input.isAnime, output.isAnime)
        assertEquals(input.isManga, output.isManga)
    }

    override fun assertsService(input: RevisarDto?) {
        assertNotNull(input)
        assertNotNull(input!!.vocabulario)
        assertNotNull(input.traducao)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.vocabulario, aux.vocabulario)
        assertEquals(input.formaBasica, aux.formaBasica)
        assertEquals(input.leitura, aux.leitura)
        assertEquals(input.traducao, aux.traducao)
        assertEquals(input.ingles, aux.ingles)
        assertEquals(input.revisado, aux.revisado)
        assertEquals(input.aparece, aux.aparece)
        assertEquals(input.isAnime, aux.isAnime)
        assertEquals(input.isManga, aux.isManga)
    }
}