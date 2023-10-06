package br.com.fenix.apiIntegracao.config

import br.com.fenix.apiIntegracao.security.JwtConfigurer
import br.com.fenix.apiIntegracao.security.JwtTokenProvider
import br.com.fenix.apiIntegracao.service.api.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class SecurityConfig(var userDetailsService: UsuarioService) {

    companion object {
        fun getPasswordEncoder() : PasswordEncoder = BCryptPasswordEncoder()
        fun encodePassword(password: String): String = getPasswordEncoder().encode(password)
    }

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Bean
    fun passwordEncoder(): PasswordEncoder = getPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and().build()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .httpBasic().disable()
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity> ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/auth/signin",
                    "/auth/refresh",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/users").denyAll()
            }
            .cors()
            .and()
            .apply(JwtConfigurer(tokenProvider))
            .and()
            .build()
    }
}