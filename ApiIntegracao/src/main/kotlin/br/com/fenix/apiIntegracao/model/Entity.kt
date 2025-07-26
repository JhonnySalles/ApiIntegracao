package br.com.fenix.apiIntegracao.model

interface Entity<T, ID> {
    fun merge(source: T)
    fun patch(source: T)
    fun getId(): ID
    fun create(id: ID): T
}