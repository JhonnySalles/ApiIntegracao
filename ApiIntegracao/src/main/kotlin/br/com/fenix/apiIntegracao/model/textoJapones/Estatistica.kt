package br.com.fenix.apiIntegracao.model.textoJapones

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "estatistica")
data class Estatistica(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequencial: Long,
    @Column(length = 10, nullable = false)
    var kanji: String,
    @Column(length = 10, nullable = false)
    var leitura: String,
    @Column(length = 10, nullable = false)
    var tipo: String,
    @Column(nullable = false)
    var quantidade: Double,
    @Column(nullable = false)
    var percentual: Float,
    @Column(nullable = false)
    var media: Double,
    @Column(nullable = false)
    var percMedia: Float,
    @Column(name = "CorSequencial", nullable = false)
    var corSequencial: Int
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Estatistica, Long> {

    override fun merge(source: Estatistica) {
        this.kanji = source.kanji
        this.leitura = source.leitura
        this.tipo = source.tipo
        this.quantidade = source.quantidade
        this.percentual = source.percentual
        this.media = source.media
        this.percMedia = source.percMedia
        this.corSequencial = source.corSequencial
    }

    override fun getId(): Long {
        return sequencial
    }

    override fun create(id: Long): Estatistica {
        return Estatistica(id, "", "", "", 0.0, 0f, 0.0, 0f, 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Estatistica

        if (kanji != other.kanji) return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
