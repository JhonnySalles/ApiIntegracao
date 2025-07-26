package br.com.fenix.apiintegracao.mapper

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.mock.Mock
import br.com.fenix.apiintegracao.model.Entity
import org.junit.jupiter.api.Test
import java.lang.reflect.ParameterizedType

abstract class MapperConvertBase<ID, E : Entity<E, ID>, D : DtoBase<ID>> {

    private val clazzEntity: Class<E>
    private val clazzDto: Class<D>

    init {
        val superclass = (javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
        clazzDto = superclass.actualTypeArguments[2] as Class<D>
    }

    abstract fun mock(): Mock<ID, E, D>

    @Test
    fun parseEntityToDtoTest() {
        val input = mock().mockEntity()
        val output = Mapper.parse(input, clazzDto)
        mock().assertsToDto(input, output)
    }

    @Test
    fun parseEntityListToDtoListTest() {
        val inputList = mock().mockEntityList()
        val outputList: List<D> = Mapper.parse(inputList, clazzDto)
        mock().assertsToDto(inputList[0], outputList[0])
        mock().assertsToDto(inputList[inputList.size / 2], outputList[inputList.size / 2])
        mock().assertsToDto(inputList[inputList.size - 1], outputList[inputList.size - 1])
    }

    @Test
    fun parseDtoToEntityTest() {
        val input = mock().mockDto()
        val output = Mapper.parse(input, clazzEntity)
        mock().assertsFromDto(input, output)
    }

    @Test
    fun parserDtoListToEntityListTest() {
        val inputList = mock().mockDtoList()
        val outputList: List<E> = Mapper.parse(inputList, clazzEntity)
        mock().assertsFromDto(inputList[0], outputList[0])
        mock().assertsFromDto(inputList[inputList.size / 2], outputList[inputList.size / 2])
        mock().assertsFromDto(inputList[inputList.size - 1], outputList[inputList.size - 1])
    }
}