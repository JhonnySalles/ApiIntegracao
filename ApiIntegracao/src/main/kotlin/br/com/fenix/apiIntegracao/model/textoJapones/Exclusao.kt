package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.exceptions.ResourceNonUpgradeableException
import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "exclusao")
data class Exclusao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    val exclusao: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
): Serializable, EntityBase<Exclusao, UUID?>() {

    override fun merge(source: Exclusao) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): Exclusao {
        return Exclusao(id, "" + "")
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
