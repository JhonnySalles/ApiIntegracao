package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.FilaSqlDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockFilaSql
import br.com.fenix.apiIntegracao.model.textojapones.FilaSql
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterFilaSqlTest() : MapperConvertBase<UUID?, FilaSql, FilaSqlDto>(FilaSql::class.java, FilaSqlDto::class.java) {

    lateinit var inputObject: MockFilaSql

    @BeforeEach
    fun setUp() {
        inputObject = MockFilaSql()
    }

    override fun mock(): Mock<UUID?, FilaSql, FilaSqlDto> {
        return inputObject
    }

}