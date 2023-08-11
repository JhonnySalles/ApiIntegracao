package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.exceptions.InvalidNotFoundException
import br.com.fenix.apiIntegracao.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UsuarioService(var repository: UsuarioRepository) {
    private val LOGGER = Logger.getLogger(UsuarioService::class.java.name)

    @Throws(InvalidNotFoundException::class)
    fun loadUserByUsername(login: String): UserDetails? {
        LOGGER.info("Find one user by login $login!")
        val usuario = repository.findByUsername(login)
        return usuario.orElseThrow { throw InvalidNotFoundException("Login $login not found!") }
    }
}