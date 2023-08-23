package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockVocabulario
import br.com.fenix.apiIntegracao.model.textojapones.Vocabulario
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterVocabularioTest() : MapperConvertBase<UUID?, Vocabulario, VocabularioDto>(Vocabulario::class.java, VocabularioDto::class.java) {

    lateinit var inputObject: MockVocabulario

    @BeforeEach
    fun setUp() {
        inputObject = MockVocabulario()
    }

    override fun mock(): Mock<UUID?, Vocabulario, VocabularioDto> {
        return inputObject
    }

}