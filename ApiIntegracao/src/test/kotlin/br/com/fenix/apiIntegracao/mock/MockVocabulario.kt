package br.com.fenix.apiIntegracao.mock

import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.model.textojapones.VocabularioJapones
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*

class MockVocabulario : Mock<UUID?, VocabularioJapones, VocabularioDto> {

    override fun mockEntity(): VocabularioJapones {
        return mockEntity(UUID.fromString("29344618-0c89-4439-adee-ec9529a426c6"))
    }

    override fun mockDto(): VocabularioDto {
        return mockDto(UUID.fromString("29344618-0c89-4439-adee-ec9529a426c6"))
    }

    override fun mockEntityList(): List<VocabularioJapones> {
        val list: MutableList<VocabularioJapones> = ArrayList()
        for (i in 1..13)
            list.add(mockEntity(UUID.randomUUID()))
        return list
    }

    override fun mockDtoList(): List<VocabularioDto> {
        val list: MutableList<VocabularioDto> = ArrayList()
        for (i in 1..13)
            list.add(mockDto(UUID.randomUUID()))
        return list
    }

    override fun mockEntity(id: UUID?): VocabularioJapones {
        return VocabularioJapones(
            id,
            "猫",
            "猫",
            "ねこ",
            "cat",
            "gato",
            1
        )
    }

    override fun mockDto(id: UUID?): VocabularioDto {
        return VocabularioDto(
            id,
            "猫",
            "猫",
            "ねこ",
            "cat",
            "gato",
            1
        )
    }

    override fun assertsToDto(input: VocabularioJapones, output: VocabularioDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.portugues, output.portugues)
        assertEquals(input.jlpt, output.jlpt)
    }

    override fun assertsFromDto(input: VocabularioDto, output: VocabularioJapones) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.portugues, output.portugues)
        assertEquals(input.jlpt, output.jlpt)
    }

    override fun assertsService(input: VocabularioDto?) {
        assertNotNull(input)
        assertNotNull(input!!.vocabulario)
        assertNotNull(input.portugues)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.vocabulario, aux.vocabulario)
        assertEquals(input.formaBasica, aux.formaBasica)
        assertEquals(input.leitura, aux.leitura)
        assertEquals(input.portugues, aux.portugues)
        assertEquals(input.jlpt, aux.jlpt)
    }
}