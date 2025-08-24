package br.com.fenix.apiintegracao.model.textojapones

import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "estatistica")
data class EstatisticaJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column
    var sequencial: Long?,
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
    @Column(name = "percentual_medio", nullable = false)
    var percentualMedio: Float,
    @Column(name = "cor_sequencial", nullable = false)
    var corSequencial: Int,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : EntityBase<UUID?, EstatisticaJapones>(), Serializable {

    companion object : EntityFactory<UUID?, EstatisticaJapones> {
        override fun create(id: UUID?): EstatisticaJapones = EstatisticaJapones(id, 0, "", "", "", 0.0, 0f, 0.0, 0f, 0)
    }

    override fun merge(source: EstatisticaJapones) {
        this.sequencial = source.sequencial
        this.kanji = source.kanji
        this.leitura = source.leitura
        this.tipo = source.tipo
        this.quantidade = source.quantidade
        this.percentual = source.percentual
        this.media = source.media
        this.percentualMedio = source.percentualMedio
        this.corSequencial = source.corSequencial
    }

    override fun patch(source: EstatisticaJapones) {
        if (source.sequencial != null && source.sequencial!! > 0)
            this.sequencial = source.sequencial

        if (source.kanji.isNotEmpty())
            this.kanji = source.kanji

        if (source.leitura.isNotEmpty())
            this.leitura = source.leitura

        if (source.tipo.isNotEmpty())
            this.tipo = source.tipo

        if (source.quantidade > 0)
            this.quantidade = source.quantidade

        if (source.percentual > 0)
            this.percentual = source.percentual

        if (source.media > 0)
            this.media = source.media

        if (source.percentualMedio > 0)
            this.percentualMedio = source.percentualMedio

        if (source.corSequencial > 0)
            this.corSequencial = source.corSequencial
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
        other as EstatisticaJapones
        return kanji == other.kanji
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
