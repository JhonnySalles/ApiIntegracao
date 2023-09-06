package br.com.fenix.apiIntegracao.repository.api

import br.com.fenix.apiIntegracao.model.api.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.username =:username")
    fun findByUsername(@Param("username") username: String): Optional<Usuario>

}