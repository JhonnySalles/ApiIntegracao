package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.KanjiInfoDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockKanjiInfo
import br.com.fenix.apiintegracao.model.textojapones.KanjiInfo
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterKanjiInfoTest() : MapperConvertBase<UUID?, KanjiInfo, KanjiInfoDto>() {

    lateinit var inputObject: MockKanjiInfo

    @BeforeEach
    fun setUp() {
        inputObject = MockKanjiInfo()
    }

    override fun mock(): Mock<UUID?, KanjiInfo, KanjiInfoDto> {
        return inputObject
    }

    override fun mapper(): Mapper {
        TODO("Not yet implemented")
    }

}