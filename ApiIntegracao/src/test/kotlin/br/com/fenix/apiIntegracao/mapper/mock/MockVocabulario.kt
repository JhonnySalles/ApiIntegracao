package br.com.fenix.apiIntegracao.mapper.mock

import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.model.textojapones.Vocabulario
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.*

class MockVocabulario : Mock<UUID, Vocabulario, VocabularioDto> {

    override fun mockEntity(): Vocabulario {
        return mockEntity(UUID.fromString("29344618-0c89-4439-adee-ec9529a426c6"))
    }

    override fun mockDto(): VocabularioDto {
        return mockDto(UUID.fromString("29344618-0c89-4439-adee-ec9529a426c6"))
    }

    override fun mockEntityList(): List<Vocabulario> {
        val list: MutableList<Vocabulario> = ArrayList()
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

    override fun mockEntity(id: UUID): Vocabulario {
        return Vocabulario(
            id,
            "猫",
            "猫",
            "ねこ",
            "gato"
        )
    }

    override fun mockDto(id: UUID): VocabularioDto {
        return VocabularioDto(
            id,
            "猫",
            "猫",
            "ねこ",
            "gato",
        )
    }

    override fun assertsToDto(input: Vocabulario, output: VocabularioDto) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.traducao, output.traducao)
    }

    override fun assertsFromDto(input: VocabularioDto, output: Vocabulario) {
        assertEquals(input.getId().toString(), output.getId().toString())
        assertEquals(input.vocabulario, output.vocabulario)
        assertEquals(input.formaBasica, output.formaBasica)
        assertEquals(input.leitura, output.leitura)
        assertEquals(input.traducao, output.traducao)
    }

    override fun assertsService(input: VocabularioDto?) {
        assertNotNull(input)
        assertNotNull(input!!.vocabulario)
        assertNotNull(input.traducao)

        val aux = mockEntity(UUID.randomUUID())
        assertEquals(input.vocabulario, aux.vocabulario)
        assertEquals(input.formaBasica, aux.formaBasica)
        assertEquals(input.leitura, aux.leitura)
        assertEquals(input.traducao, aux.traducao)
    }
}