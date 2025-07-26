package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.FilaSqlDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockFilaSql
import br.com.fenix.apiintegracao.model.textojapones.FilaSqlJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterFilaSqlTest() : MapperConvertBase<UUID?, FilaSqlJapones, FilaSqlDto>() {

    lateinit var inputObject: MockFilaSql

    @BeforeEach
    fun setUp() {
        inputObject = MockFilaSql()
    }

    override fun mock(): Mock<UUID?, FilaSqlJapones, FilaSqlDto> {
        return inputObject
    }

}