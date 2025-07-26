package br.com.fenix.apiintegracao.security

import br.com.fenix.apiintegracao.config.SecurityConfig


fun main(args: Array<String>) {
    println("Password 'admin': " + SecurityConfig.encodePassword("admin"))
}