package br.com.fenix.apiIntegracao.controller

import br.com.fenix.apiIntegracao.dto.api.CredencialDto
import br.com.fenix.apiIntegracao.service.api.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var authServices: AuthService

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = ["/signin"])
    fun signin(@RequestBody data: CredencialDto): ResponseEntity<*> {
        return if (checkIfParamsIsNotNull(data))
            ResponseEntity.status(HttpStatus.FORBIDDEN).body<String>("Invalid client request!")
        else
            authServices.signin(data)
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = ["/refresh/{username}"])
    fun refreshToken(
        @PathVariable("username") username: String?,
        @RequestHeader("Authorization") refreshToken: String?
    ): ResponseEntity<*>? {
        return if (checkIfParamsIsNotNull(username, refreshToken))
            ResponseEntity.status(HttpStatus.FORBIDDEN).body<String>("Invalid client request!")
        else
            authServices.refreshToken(username!!, refreshToken!!) ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).body<String>("Invalid client request!")
    }

    private fun checkIfParamsIsNotNull(username: String?, refreshToken: String?): Boolean {
        return refreshToken == null || refreshToken.isBlank() || username == null || username!!.isBlank()
    }

    private fun checkIfParamsIsNotNull(data: CredencialDto): Boolean {
        return data.username == null || data.username.isBlank() || data.password == null || data.password.isBlank()
    }

}