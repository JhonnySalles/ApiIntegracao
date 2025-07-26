package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "vocabulario")
data class VocabularioJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    val vocabulario: String,
    @Column(length = 250, nullable = false)
    var formaBasica: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(length = 250, nullable = false)
    var ingles: String,
    @Column(nullable = false)
    var portugues: String,
    @Column(nullable = false)
    var jlpt: Int,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<VocabularioJapones, UUID?>() {

    override fun merge(source: VocabularioJapones) {
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.ingles = source.ingles
        this.portugues = source.portugues
        this.jlpt = source.jlpt
    }

    override fun patch(source: VocabularioJapones) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): VocabularioJapones {
        return VocabularioJapones(id, "", "", "", "", "", 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VocabularioJapones

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
