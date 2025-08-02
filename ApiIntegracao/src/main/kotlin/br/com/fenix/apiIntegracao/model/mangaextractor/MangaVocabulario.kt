package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "vocabulario")
data class MangaVocabulario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    val palavra: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(length = 250, nullable = false)
    var ingles: String,
    @Column(nullable = false)
    var portugues: String,
    @Column
    var revisado: Boolean,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<UUID?, MangaVocabulario>() {

    override fun merge(source: MangaVocabulario) {
        this.leitura = source.leitura
        this.ingles = source.ingles
        this.portugues = source.portugues
        this.revisado = source.revisado
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: MangaVocabulario) {
        if (source.leitura.isNotEmpty())
            this.leitura = source.leitura

        if (source.ingles.isNotEmpty())
            this.ingles = source.ingles

        if (source.portugues.isNotEmpty())
            this.portugues = source.portugues

        this.atualizacao = LocalDateTime.now()
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): MangaVocabulario {
        return MangaVocabulario(id, "", "", "", "", false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaVocabulario
        return palavra == other.palavra
    }

    override fun hashCode(): Int {
        return palavra.hashCode()
    }

}
