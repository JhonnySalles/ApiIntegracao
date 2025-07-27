package br.com.fenix.apiintegracao.model.textoingles

import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "vocabulario")
data class VocabularioIngles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    val vocabulario: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(nullable = false)
    var portugues: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<UUID?, VocabularioIngles>() {

    override fun merge(source: VocabularioIngles) {
        this.leitura = source.leitura
        this.portugues = source.portugues
    }

    override fun patch(source: VocabularioIngles) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): VocabularioIngles {
        return VocabularioIngles(id, "", "", "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VocabularioIngles

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
