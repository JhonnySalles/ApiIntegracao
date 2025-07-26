package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockRevisar
import br.com.fenix.apiintegracao.model.textojapones.RevisarJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterRevisarTest() : MapperConvertBase<UUID?, RevisarJapones, RevisarDto>() {

    lateinit var inputObject: MockRevisar

    @BeforeEach
    fun setUp() {
        inputObject = MockRevisar()
    }

    override fun mock(): Mock<UUID?, RevisarJapones, RevisarDto> {
        return inputObject
    }

}