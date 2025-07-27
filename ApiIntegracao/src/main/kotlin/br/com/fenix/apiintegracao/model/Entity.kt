package br.com.fenix.apiintegracao.model

interface Entity<ID, E> {
    fun merge(source: E)
    fun patch(source: E)
    fun getId(): ID
    fun setId(id: ID)
    fun create(id: ID): E
}