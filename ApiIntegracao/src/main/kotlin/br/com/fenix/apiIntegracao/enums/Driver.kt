package br.com.fenix.apiIntegracao.enums

enum class Driver(val descricao: String) {
    MYSQL("MYSQL"), POSTGRE("POSTGRE");

    override fun toString(): String {
        return descricao
    }
}