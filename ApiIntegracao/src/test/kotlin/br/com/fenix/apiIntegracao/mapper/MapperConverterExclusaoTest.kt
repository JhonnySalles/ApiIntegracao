package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.mapper.mock.MockExclusao
import br.com.fenix.apiIntegracao.model.textojapones.ExclusaoJapones
import org.junit.jupiter.api.BeforeEach
import java.util.*


class MapperConverterExclusaoTest() : MapperConvertBase<UUID?, ExclusaoJapones, ExclusaoDto>(ExclusaoJapones::class.java, ExclusaoDto::class.java) {

    lateinit var inputObject: MockExclusao

    @BeforeEach
    fun setUp() {
        inputObject = MockExclusao()
    }

    override fun mock(): Mock<UUID?, ExclusaoJapones, ExclusaoDto> {
        return inputObject
    }

}