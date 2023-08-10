package br.com.fenix.apiIntegracao.mapper.mock

interface Mock<ID, E, D> {
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