package br.com.fenix.apiintegracao.model

interface EntityFactory<ID, E> {
    fun create(id: ID): E
}