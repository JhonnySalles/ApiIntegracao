package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockRevisar
import br.com.fenix.apiIntegracao.model.textojapones.RevisarJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterRevisarTest() : MapperConvertBase<UUID?, RevisarJapones, RevisarDto>(RevisarJapones::class.java, RevisarDto::class.java) {

    lateinit var inputObject: MockRevisar

    @BeforeEach
    fun setUp() {
        inputObject = MockRevisar()
    }

    override fun mock(): Mock<UUID?, RevisarJapones, RevisarDto> {
        return inputObject
    }

}