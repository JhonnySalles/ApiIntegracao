package br.com.fenix.apiintegracao.controller.api

import br.com.fenix.apiintegracao.dto.api.CredencialDto
import br.com.fenix.apiintegracao.service.api.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var authServices: AuthService

    @Operation(
        summary = "Authenticates a user and returns a token",
        description = """Este endpoint realiza a autenticação de um usuário no sistema.

**Corpo da Requisição:**
* Aceita um objeto de credenciais (`CredencialDto`) contendo `username` e `password`.

**Corpo da Resposta (Sucesso):**
* Em caso de sucesso (`200 OK`), retorna um objeto contendo os tokens de acesso. A estrutura geralmente inclui:
  * `accessToken`: O token JWT a ser usado para autenticar requisições subsequentes.
  * `refreshToken`: O token a ser usado para renovar o `accessToken` quando ele expirar.
  * Informações de data de criação e expiração.

**Corpo da Resposta (Falha):**
* Retorna um status `403 Forbidden` caso as credenciais sejam inválidas ou os parâmetros estejam ausentes."""
    )
    @PostMapping(value = ["/signin"])
    fun signin(@RequestBody data: CredencialDto): ResponseEntity<*> {
        return if (checkIfParamsIsNotNull(data))
            ResponseEntity.status(HttpStatus.FORBIDDEN).body<String>("Invalid client request!")
        else
            authServices.signin(data)
    }

    @Operation(
        summary = "Refresh token for authenticated user and returns a token",
        description = """Este endpoint é usado para renovar um `accessToken` expirado utilizando um `refreshToken` válido.
                        
**Parâmetros de Path:**
* **`username`**: O nome de usuário para o qual o token deve ser renovado.

**Cabeçalho da Requisição (Header):**
* **`Authorization`**: Deve conter o `refreshToken` válido. Geralmente enviado no formato `Bearer [seu_refresh_token]`.

**Corpo da Resposta (Sucesso):**
* Em caso de sucesso (`200 OK`), retorna um novo conjunto de tokens (um novo `accessToken` e, opcionalmente, um novo `refreshToken`).

**Corpo da Resposta (Falha):**
* Retorna um status `403 Forbidden` se o `refreshToken` for inválido, expirado, ou se os parâmetros da requisição estiverem incorretos."""
    )
    @PutMapping(value = ["/refresh/{username}"])
    fun refreshToken(@PathVariable("username") username: String?, @RequestHeader("Authorization") refreshToken: String?): ResponseEntity<*>? {
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