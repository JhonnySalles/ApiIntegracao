package br.com.fenix.apiintegracao.dto

interface Dto<ID> {
    fun getId(): ID
    fun setId(id: ID)
}