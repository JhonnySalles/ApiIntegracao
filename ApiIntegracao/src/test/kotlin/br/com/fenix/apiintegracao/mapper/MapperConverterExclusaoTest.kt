package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.mock.MockExclusao
import br.com.fenix.apiintegracao.model.textojapones.ExclusaoJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*

class MapperConverterExclusaoTest() : MapperConvertBase<UUID?, ExclusaoJapones, ExclusaoDto>() {

    lateinit var inputObject: MockExclusao

    @BeforeEach
    fun setUp() {
        inputObject = MockExclusao()
    }

    override fun mock(): Mock<UUID?, ExclusaoJapones, ExclusaoDto> {
        return inputObject
    }

    override fun mapper(): Mapper {
        TODO("Not yet implemented")
    }

}