package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.mock.Mock
import br.com.fenix.apiIntegracao.mock.MockEstatistica
import br.com.fenix.apiIntegracao.model.textojapones.EstatisticaJapones
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