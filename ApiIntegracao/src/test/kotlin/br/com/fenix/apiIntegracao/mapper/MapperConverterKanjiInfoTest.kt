package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.KanjiInfoDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockKanjiInfo
import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterKanjiInfoTest() : MapperConvertBase<UUID?, KanjiInfo, KanjiInfoDto>(KanjiInfo::class.java, KanjiInfoDto::class.java) {

    lateinit var inputObject: MockKanjiInfo

    @BeforeEach
    fun setUp() {
        inputObject = MockKanjiInfo()
    }

    override fun mock(): Mock<UUID?, KanjiInfo, KanjiInfoDto> {
        return inputObject
    }

}