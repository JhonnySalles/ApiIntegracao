package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockEstatistica
import br.com.fenix.apiintegracao.model.textojapones.EstatisticaJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterEstatisticaTest() : MapperConvertBase<UUID?, EstatisticaJapones, EstatisticaDto>() {

    lateinit var inputObject: MockEstatistica

    @BeforeEach
    fun setUp() {
        inputObject = MockEstatistica()
    }

    override fun mock(): Mock<UUID?, EstatisticaJapones, EstatisticaDto> {
        return inputObject
    }

}