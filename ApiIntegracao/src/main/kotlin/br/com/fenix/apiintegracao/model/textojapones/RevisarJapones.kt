package br.com.fenix.apiintegracao.model.textojapones

import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
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
    val vocabulario: String,
    @Column(name = "forma_basica", length = 250, nullable = false)
    var formaBasica: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(name = "leitura_novel", length = 250, nullable = false)
    var leituraNovel: String,
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
) : Serializable, EntityBase<UUID?, RevisarJapones>() {

    companion object : EntityFactory<UUID?, RevisarJapones> {
        override fun create(id: UUID?): RevisarJapones = RevisarJapones(id, "", "", "", "", "", "", false, 0, isAnime = false, isManga = false)
    }

    override fun merge(source: RevisarJapones) {
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.leituraNovel = source.leituraNovel
        this.portugues = source.portugues
        this.ingles = source.ingles
        this.revisado = source.revisado
        this.aparece = source.aparece
        this.isAnime = source.isAnime
        this.isManga = source.isManga
    }

    override fun patch(source: RevisarJapones) {
        if (source.formaBasica.isNotEmpty())
            this.formaBasica = source.formaBasica

        if (source.leitura.isNotEmpty())
            this.leitura = source.leitura

        if (source.leituraNovel.isNotEmpty())
            this.leituraNovel = source.leituraNovel

        if (source.portugues.isNotEmpty())
            this.portugues = source.portugues

        if (source.ingles.isNotEmpty())
            this.ingles = source.ingles

        if (source.aparece > 0)
            this.aparece = source.aparece
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
        other as RevisarJapones
        return vocabulario == other.vocabulario
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
