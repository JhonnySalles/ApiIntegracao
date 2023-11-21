package br.com.fenix.apiIntegracao.service.api

import br.com.fenix.apiIntegracao.exceptions.InvalidNotFoundException
import br.com.fenix.apiIntegracao.repository.api.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UsuarioService(var repository: UsuarioRepository) : UserDetailsService {
    private val LOGGER = Logger.getLogger(UsuarioService::class.java.name)

    @Throws(InvalidNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        LOGGER.info("Find one user by username $username!")
        val usuario = repository.findByUsername(username)
        return usuario.orElseThrow { throw InvalidNotFoundException("Username $username not found!") }
    }
}