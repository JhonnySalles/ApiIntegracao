package br.com.fenix.apiIntegracao.service.api

import br.com.fenix.apiIntegracao.exceptions.InvalidNotFoundException
import br.com.fenix.apiIntegracao.repository.api.UsuarioRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UsuarioService(@Autowired val repository: UsuarioRepository) : UserDetailsService {
    private val oLog = LoggerFactory.getLogger(UsuarioService::class.java.name)

    @Throws(InvalidNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        oLog.info("Find one user by username $username!")
        val usuario = repository.findByUsername(username)
        return usuario.orElseThrow { throw InvalidNotFoundException("Username $username not found!") }
    }
}