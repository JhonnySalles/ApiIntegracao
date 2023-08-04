package br.com.fenix.apiIntegracao.model.decksubtitle

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "")
data class Legendas(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequencial: Long,
    @Column(nullable = false)
    var episodio: Int,
    @Column(length = 10, nullable = false)
    var linguagem: String,
    @Column(length = 15, nullable = false)
    var tempoInicial: String,
    @Column(length = 15)
    var tempoFinal: String?,
    @Column(nullable = false)
    var texto: String,
    @Column(nullable = false)
    var traducao: String,
    @Column
    var vocabulario: String?
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Legendas, Long> {

    override fun merge(source: Legendas) {
        this.episodio = source.episodio
        this.tempoInicial = source.tempoInicial
        this.tempoFinal = source.tempoFinal
        this.linguagem = source.linguagem
        this.traducao = source.traducao
        this.vocabulario = source.vocabulario
        this.texto = source.texto
    }

    override fun getId(): Long {
        return sequencial
    }

    override fun create(id: Long): Legendas {
        return Legendas(id, 0, "", "", null, "", "", null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Legendas

        if (sequencial != other.sequencial) return false

        return true
    }

    override fun hashCode(): Int {
        return sequencial.hashCode()
    }
}
