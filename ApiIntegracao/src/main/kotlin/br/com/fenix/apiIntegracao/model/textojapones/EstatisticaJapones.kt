package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.model.EntityBase
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
    @Column(nullable = false)
    var percentualMedio: Float,
    @Column(name = "CorSequencial", nullable = false)
    var corSequencial: Int,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : EntityBase<EstatisticaJapones, UUID?>(), Serializable {

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

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): EstatisticaJapones {
        return EstatisticaJapones(id, 0, "", "", "", 0.0, 0f, 0.0, 0f, 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EstatisticaJapones

        if (kanji != other.kanji) return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
