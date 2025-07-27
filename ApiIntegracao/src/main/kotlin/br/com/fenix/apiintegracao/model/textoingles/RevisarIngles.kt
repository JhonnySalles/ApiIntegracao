package br.com.fenix.apiintegracao.model.textoingles

import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "revisar")
data class RevisarIngles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    var vocabulario: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
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
) : Serializable, EntityBase<UUID?, RevisarIngles>() {

    override fun merge(source: RevisarIngles) {
        this.vocabulario = source.vocabulario
        this.leitura = source.leitura
        this.portugues = source.portugues
        this.revisado = source.revisado
        this.aparece = source.aparece
        this.isAnime = source.isAnime
        this.isManga = source.isManga
    }

    override fun patch(source: RevisarIngles) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): RevisarIngles {
        return RevisarIngles(id, "", "", "",  false, 0, isAnime = false, isManga = false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RevisarIngles

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
