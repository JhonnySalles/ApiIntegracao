package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockVocabulario
import br.com.fenix.apiintegracao.model.textojapones.VocabularioJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterMangaVocabularioTest() : MapperConvertBase<UUID?, VocabularioJapones, VocabularioDto>() {

    lateinit var inputObject: MockVocabulario

    @BeforeEach
    fun setUp() {
        inputObject = MockVocabulario()
    }

    override fun mock(): Mock<UUID?, VocabularioJapones, VocabularioDto> {
        return inputObject
    }

    override fun mapper(): Mapper {
        TODO("Not yet implemented")
    }

}