package br.com.fenix.apiIntegracao.model.textoingles

import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "revisar")
data class Valido(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    var palavra: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<Valido, UUID?>() {

    override fun merge(source: Valido) {
        this.palavra = source.palavra
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): Valido {
        return Valido(id, "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Valido

        if (palavra != other.palavra) return false

        return true
    }

    override fun hashCode(): Int {
        return palavra.hashCode()
    }
}
