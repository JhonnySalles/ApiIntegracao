package br.com.fenix.apiintegracao.model.textoingles

import br.com.fenix.apiintegracao.exceptions.ResourceNonUpgradeableException
import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "valido")
data class ValidoIngles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    val palavra: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<UUID?, ValidoIngles>() {

    override fun merge(source: ValidoIngles) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun patch(source: ValidoIngles) {
        throw ResourceNonUpgradeableException("Recurso não atualizável: $source")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): ValidoIngles {
        return ValidoIngles(id, "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ValidoIngles
        return palavra == other.palavra
    }

    override fun hashCode(): Int {
        return palavra.hashCode()
    }
}
