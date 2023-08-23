package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockRevisar
import br.com.fenix.apiIntegracao.model.textojapones.Revisar
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterRevisarTest() : MapperConvertBase<UUID?, Revisar, RevisarDto>(Revisar::class.java, RevisarDto::class.java) {

    lateinit var inputObject: MockRevisar

    @BeforeEach
    fun setUp() {
        inputObject = MockRevisar()
    }

    override fun mock(): Mock<UUID?, Revisar, RevisarDto> {
        return inputObject
    }

}