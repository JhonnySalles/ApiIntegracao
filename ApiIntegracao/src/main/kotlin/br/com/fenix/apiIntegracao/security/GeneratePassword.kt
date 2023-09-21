package br.com.fenix.apiIntegracao.security

import br.com.fenix.apiIntegracao.config.SecurityConfig


fun main(args: Array<String>) {
    println("Password 'admin': " + SecurityConfig.encodePassword("admin"))
}