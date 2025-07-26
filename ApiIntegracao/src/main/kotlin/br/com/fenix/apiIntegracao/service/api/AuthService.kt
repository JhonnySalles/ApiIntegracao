package br.com.fenix.apiIntegracao.service.api

import br.com.fenix.apiIntegracao.dto.api.CredencialDto
import br.com.fenix.apiIntegracao.dto.api.TokenDto
import br.com.fenix.apiIntegracao.repository.api.UsuarioRepository
import br.com.fenix.apiIntegracao.security.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class AuthService {

    companion object {
        private val oLog = LoggerFactory.getLogger(AuthService::class.java.name)
    }

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var repository: UsuarioRepository

    fun signin(data: CredencialDto): ResponseEntity<TokenDto> {
        return try {
            val username = data.username!!
            val password = data.password!!
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            val user = repository.findByUsername(username).orElseThrow { throw UsernameNotFoundException("Username $username not found!") }
            val tokenResponse = tokenProvider.createAccessToken(username, user.getRoles())
            ResponseEntity.ok(tokenResponse)
        } catch (e: Exception) {
            oLog.error("Error when singin: " + e.message)
            throw BadCredentialsException("Invalid username/password supplied!")
        }
    }

    fun refreshToken(username: String, refreshToken: String): ResponseEntity<*>? {
        val user = repository.findByUsername(username).orElseThrow { throw UsernameNotFoundException("Username $username not found!") }
        val tokenResponse = tokenProvider.refreshToken(refreshToken)
        return ResponseEntity.ok<Any>(tokenResponse)
    }
}