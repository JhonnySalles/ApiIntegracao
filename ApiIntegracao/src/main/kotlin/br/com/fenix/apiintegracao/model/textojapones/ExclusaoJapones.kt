package br.com.fenix.apiintegracao.model.textojapones

import br.com.fenix.apiintegracao.exceptions.ResourceNonUpgradeableException
import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "exclusao")
data class ExclusaoJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(name = "palavra", length = 250, nullable = false)
    val exclusao: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
): Serializable, EntityBase<UUID?, ExclusaoJapones>() {

    override fun merge(source: ExclusaoJapones) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun patch(source: ExclusaoJapones) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): ExclusaoJapones {
        return ExclusaoJapones(id, "" + "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ExclusaoJapones
        return exclusao == other.exclusao
    }

    override fun hashCode(): Int {
        return exclusao.hashCode()
    }
}
