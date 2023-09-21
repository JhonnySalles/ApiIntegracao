package br.com.fenix.apiIntegracao.security


import br.com.fenix.apiIntegracao.dto.api.TokenDto
import br.com.fenix.apiIntegracao.exceptions.InvalidAuthenticationException
import br.com.fenix.apiIntegracao.service.api.UsuarioService
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*


@Service
class JwtTokenProvider {

    @Value("\${security.jwt.token.secret-key:secret}")
    private var secretKey = "secret"

    @Value("\${security.jwt.token.expire-length:3600000}")
    private val validity: Long = 3600000 * 6 // 6h

    @Autowired
    private lateinit var service: UsuarioService

    private lateinit var algorithm: Algorithm

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    }

    fun createAccessToken(username: String, roles: List<String>): TokenDto {
        val now = Date()
        val validity = Date(now.time + validity)
        val accessToken = getAccessToken(username, roles, now, validity)
        val refreshToken = getRefreshToken(username, roles, now)
        return TokenDto(username, true, now, validity, accessToken, refreshToken)
    }


    fun refreshToken(refreshToken: String): TokenDto {
        var token = refreshToken
        if (token.contains("Bearer ")) token = token.substring("Bearer ".length)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
        val username: String = decodedJWT.subject
        val roles: List<String> = decodedJWT.getClaim("roles").asList(String::class.java)
        return createAccessToken(username, roles)
    }

    private fun getAccessToken(username: String, roles: List<String>, now: Date, validity: Date): String {
        val issuerUrl = ServletUriComponentsBuilder
            .fromCurrentContextPath().build().toUriString()
        return JWT.create()
            .withClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .withSubject(username)
            .withIssuer(issuerUrl)
            .sign(algorithm)
            .strip()
    }

    private fun getRefreshToken(username: String, roles: List<String>, now: Date): String {
        val validityRefreshToken = Date(now.time + validity * 3)
        return JWT.create()
            .withClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(validityRefreshToken)
            .withSubject(username)
            .sign(algorithm)
            .strip()
    }

    fun getAuthentication(token: String): Authentication {
        val decodedJWT: DecodedJWT = decodedToken(token)
        val userDetails = service.loadUserByUsername(decodedJWT.subject)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun decodedToken(token: String): DecodedJWT {
        val alg: Algorithm = Algorithm.HMAC256(secretKey.toByteArray())
        val verifier: JWTVerifier = JWT.require(alg).build()
        return verifier.verify(token)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")

        // Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsZWFuZHJvIiwicm9sZXMiOlsiQURNSU4iLCJNQU5BR0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImV4cCI6MTY1MjcxOTUzOCwiaWF0IjoxNjUyNzE1OTM4fQ.muu8eStsRobqLyrFYLHRiEvOSHAcss4ohSNtmwWTRcY
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring("Bearer ".length)
        } else null
    }

    fun validateToken(token: String): Boolean {
        val decodedJWT: DecodedJWT = decodedToken(token)
        return try {
            !decodedJWT.expiresAt.before(Date())
        } catch (e: Exception) {
            throw InvalidAuthenticationException("Expired or invalid JWT token!")
        }
    }
}