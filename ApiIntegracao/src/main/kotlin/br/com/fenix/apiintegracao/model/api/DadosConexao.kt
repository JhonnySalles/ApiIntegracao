package br.com.fenix.apiintegracao.model.api

import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.enums.Driver
import br.com.fenix.apiintegracao.enums.Mapeamento
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "conexoes")
data class DadosConexao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true, length = 11)
    private var id: Long?,
    @Enumerated(EnumType.STRING)
    val tipo: Conexao,
    @Column(length = 100, nullable = true)
    val url: String,
    @Column(name = "base", length = 100, nullable = true)
    val base: String,
    @Column(name = "username", length = 250, nullable = true)
    val usuario: String,
    @Column(name = "password", length = 250, nullable = true)
    val senha: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "driver")
    val driver: Driver,
    @Enumerated(EnumType.STRING)
    @Column
    val mapeamento: Mapeamento,
    @Column
    val ativo: Boolean
) : Serializable {

    constructor(url: String, base: String, usuario: String, senha: String, ativo: Boolean) : this(0, Conexao.API, url, base, usuario, senha, Driver.MYSQL, Mapeamento.JDBC, ativo)
    constructor() : this(0, Conexao.API, "", "", "", "", Driver.MYSQL, Mapeamento.JDBC, false)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DadosConexao
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}