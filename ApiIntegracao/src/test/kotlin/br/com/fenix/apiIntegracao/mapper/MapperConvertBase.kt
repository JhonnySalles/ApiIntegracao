package br.com.fenix.apiIntegracao.mapper

import br.com.fenix.apiIntegracao.dto.DtoBase
import br.com.fenix.apiIntegracao.mapper.mock.Mock
import br.com.fenix.apiIntegracao.model.Entity
import org.junit.jupiter.api.Test

abstract class MapperConvertBase<ID, E : Entity<E, ID>, D : DtoBase<ID>>(var entity: Class<E>, var dto: Class<D>) {

    abstract fun mock(): Mock<ID, E, D>

    @Test
    fun parseEntityToDtoTest() {
        val input = mock().mockEntity()
        val output = Mapper.parse(input, dto)
        mock().assertsToDto(input, output)
    }

    @Test
    fun parseEntityListToDtoListTest() {
        val inputList = mock().mockEntityList()
        val outputList: List<D> = Mapper.parse(inputList, dto)
        mock().assertsToDto(inputList[0], outputList[0])
        mock().assertsToDto(inputList[inputList.size / 2], outputList[inputList.size / 2])
        mock().assertsToDto(inputList[inputList.size - 1], outputList[inputList.size - 1])
    }

    @Test
    fun parseDtoToEntityTest() {
        val input = mock().mockDto()
        val output = Mapper.parse(input, entity)
        mock().assertsFromDto(input, output)
    }

    @Test
    fun parserDtoListToEntityListTest() {
        val inputList = mock().mockDtoList()
        val outputList: List<E> = Mapper.parse(inputList, entity)
        mock().assertsFromDto(inputList[0], outputList[0])
        mock().assertsFromDto(inputList[inputList.size / 2], outputList[inputList.size / 2])
        mock().assertsFromDto(inputList[inputList.size - 1], outputList[inputList.size - 1])
    }
}