package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.exceptions.ResourceNonUpgradeableException
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "exclusao")
data class Exclusao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: String?,
    @Column(length = 250, nullable = false)
    val exclusao: String
): Serializable, br.com.fenix.apiIntegracao.model.Entity<Exclusao, String?> {

    override fun merge(source: Exclusao) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun getId(): String? {
        return id
    }

    override fun create(id: String?): Exclusao {
        return Exclusao(id, "" +
                "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exclusao

        if (exclusao != other.exclusao) return false

        return true
    }

    override fun hashCode(): Int {
        return exclusao.hashCode()
    }
}
