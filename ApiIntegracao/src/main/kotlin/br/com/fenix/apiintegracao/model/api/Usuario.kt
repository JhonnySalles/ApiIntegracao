package br.com.fenix.apiintegracao.model.api

import jakarta.persistence.*
import jakarta.persistence.Entity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(unique = true)
    private val username: String,
    @Column
    val nome: String,
    @Column
    private val password: String,
    @Column(name = "conta_nao_expirada")
    val contaNaoExpirada: Boolean,
    @Column(name = "conta_nao_travada")
    val contaNaoTravada: Boolean,
    @Column(name = "credencial_nao_expirado")
    val credencialNaoExpirada: Boolean,
    @Column(name = "ativo")
    val ativo: Boolean,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_permissoes",
        joinColumns = [JoinColumn(name = "id_usuario")],
        inverseJoinColumns = [JoinColumn(name = "id_permissao")]
    )
    val permissoes: MutableList<Permissao>,
) : UserDetails, Serializable {

    fun getRoles(): List<String> {
        val roles: MutableList<String> = ArrayList()
        for (permission in permissoes)
            roles.add(permission.descricao)
        return roles
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return permissoes
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return contaNaoExpirada
    }

    override fun isAccountNonLocked(): Boolean {
        return contaNaoTravada
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credencialNaoExpirada
    }

    override fun isEnabled(): Boolean {
        return ativo
    }

}