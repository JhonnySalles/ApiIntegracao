package br.com.fenix.apiintegracao.model.textoingles

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.exceptions.ResourceNonUpgradeableException
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapa
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "exclusao")
data class ExclusaoIngles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(name = "palavra", length = 250, nullable = false)
    val exclusao: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
): Serializable, EntityBase<UUID?, ExclusaoIngles>() {

    companion object : EntityFactory<UUID?, ExclusaoIngles> {
        override fun create(id: UUID?): ExclusaoIngles = ExclusaoIngles(id, "" + "")
    }

    override fun merge(source: ExclusaoIngles) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun patch(source: ExclusaoIngles) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExclusaoIngles

        if (exclusao != other.exclusao) return false

        return true
    }

    override fun hashCode(): Int {
        return exclusao.hashCode()
    }
}
