package br.com.fenix.apiIntegracao.mock

import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.model.textojapones.RevisarJapones
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*
import kotlin.random.Random

class MockRevisar : Mock<UUID?, RevisarJapones, RevisarDto> {

    override fun mockEntity(): RevisarJapones {
        return mockEntity(UUID.fromString("b4822003-8f53-4c22-a272-5392c1c68c5e"))
    }

    override fun mockDto(): RevisarDto {
        return mockDto(UUID.fromString("b4822003-8f53-4c22-a272-5392c1c68c5e"))
    }

    override fun mockEntityList(): List<RevisarJapones> {
        val list: MutableList<RevisarJapones> = ArrayList()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<RevisarDto> {
        val list: MutableList<RevisarDto> = ArrayList()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): RevisarJapones {
        return RevisarJapones(
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

    override fun mockDto(id: UUID?): RevisarDto {
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

    override fun assertsToDto(input: RevisarJapones, output: RevisarDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.ingles, output.ingles)
        assertEquals(input.portugues, output.portugues)
        assertEquals(input.revisado, output.revisado)
        assertEquals(input.aparece, output.aparece)
        assertEquals(input.isAnime, output.isAnime)
        assertEquals(input.isManga, output.isManga)
    }

    override fun assertsFromDto(input: RevisarDto, output: RevisarJapones) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.ingles, output.ingles)
        assertEquals(input.portugues, output.portugues)
        assertEquals(input.revisado, output.revisado)
        assertEquals(input.aparece, output.aparece)
        assertEquals(input.isAnime, output.isAnime)
        assertEquals(input.isManga, output.isManga)
    }

    override fun assertsService(input: RevisarDto?) {
        assertNotNull(input)
        assertNotNull(input!!.vocabulario)
        assertNotNull(input.portugues)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.vocabulario, aux.vocabulario)
        assertEquals(input.formaBasica, aux.formaBasica)
        assertEquals(input.leitura, aux.leitura)
        assertEquals(input.ingles, aux.ingles)
        assertEquals(input.portugues, aux.portugues)
        assertEquals(input.revisado, aux.revisado)
        assertEquals(input.aparece, aux.aparece)
        assertEquals(input.isAnime, aux.isAnime)
        assertEquals(input.isManga, aux.isManga)
    }
}