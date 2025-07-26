package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "revisar")
data class RevisarJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    var vocabulario: String,
    @Column(length = 250, nullable = false)
    var formaBasica: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(nullable = false)
    var ingles: String,
    @Column(nullable = false)
    var portugues: String,
    @Column(nullable = false)
    var revisado: Boolean,
    @Column(nullable = false)
    var aparece: Int,
    @Column(nullable = false)
    var isAnime: Boolean,
    @Column(nullable = false)
    var isManga: Boolean,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<RevisarJapones, UUID?>() {

    override fun merge(source: RevisarJapones) {
        this.vocabulario = source.vocabulario
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.portugues = source.portugues
        this.ingles = source.ingles
        this.portugues = source.portugues
        this.revisado = source.revisado
        this.aparece = source.aparece
        this.isAnime = source.isAnime
        this.isManga = source.isManga
    }

    override fun patch(source: RevisarJapones) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): RevisarJapones {
        return RevisarJapones(id, "", "", "", "", "", false, 0, isAnime = false, isManga = false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RevisarJapones

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
