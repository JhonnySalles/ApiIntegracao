package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.FilaSqlDto
import br.com.fenix.apiIntegracao.mock.Mock
import br.com.fenix.apiIntegracao.mock.MockFilaSql
import br.com.fenix.apiIntegracao.model.textojapones.FilaSqlJapones
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