package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockExclusao
import br.com.fenix.apiIntegracao.model.textojapones.Exclusao
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterExclusaoTest() : MapperConvertBase<UUID, Exclusao, ExclusaoDto>(Exclusao::class.java, ExclusaoDto::class.java) {

    lateinit var inputObject: MockExclusao

    @BeforeEach
    fun setUp() {
        inputObject = MockExclusao()
    }

    override fun mock(): Mock<UUID, Exclusao, ExclusaoDto> {
        return inputObject
    }

}