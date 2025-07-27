package br.com.fenix.apiintegracao.model.textojapones

import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "vocabulario")
data class VocabularioJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JdbcTypeCode(SqlTypes.VARCHAR)
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
    @Column(length = 250, nullable = false)
    var ingles: String,
    @Column(nullable = false)
    var portugues: String,
    @Column(nullable = false)
    var jlpt: Int,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<UUID?, VocabularioJapones>() {

    override fun merge(source: VocabularioJapones) {
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.leituraNovel = source.leituraNovel
        this.ingles = source.ingles
        this.portugues = source.portugues
        this.jlpt = source.jlpt
    }

    override fun patch(source: VocabularioJapones) {
        if (source.formaBasica.isNotEmpty())
            this.formaBasica = source.formaBasica

        if (source.formaBasica.isNotEmpty())
            this.leitura = source.leitura

        if (source.leituraNovel.isNotEmpty())
            this.leituraNovel = source.leituraNovel

        if (source.ingles.isNotEmpty())
            this.ingles = source.ingles

        if (source.portugues.isNotEmpty())
            this.portugues = source.portugues

        if (source.jlpt > 0)
            this.jlpt = source.jlpt
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): VocabularioJapones {
        return VocabularioJapones(id, "", "", "", "", "", "",0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as VocabularioJapones
        return vocabulario == other.vocabulario
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
