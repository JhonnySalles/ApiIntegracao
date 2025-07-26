package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.mock.Mock
import br.com.fenix.apiIntegracao.mock.MockVocabulario
import br.com.fenix.apiIntegracao.model.textojapones.VocabularioJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterVocabularioTest() : MapperConvertBase<UUID?, VocabularioJapones, VocabularioDto>() {

    lateinit var inputObject: MockVocabulario

    @BeforeEach
    fun setUp() {
        inputObject = MockVocabulario()
    }

    override fun mock(): Mock<UUID?, VocabularioJapones, VocabularioDto> {
        return inputObject
    }

}