package br.com.fenix.apiIntegracao.mock

import br.com.fenix.apiIntegracao.dto.DtoBase
import br.com.fenix.apiIntegracao.model.Entity

interface Mock<ID, E : Entity<E, ID>, D : DtoBase<ID>> {
    fun mockEntity(): E
    fun mockDto(): D
    fun mockEntityList(): List<E>
    fun mockDtoList(): List<D>
    fun mockEntity(id: ID): E
    fun mockDto(id: ID): D
    fun assertsToDto(input: E, output: D)
    fun assertsFromDto(input: D, output: E)
    fun assertsService(input: D?)
}