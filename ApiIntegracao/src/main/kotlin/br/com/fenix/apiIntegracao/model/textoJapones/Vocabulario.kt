package br.com.fenix.apiIntegracao.model.textoJapones

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "vocabulario")
data class Vocabulario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 250, nullable = false)
    val vocabulario: String,
    @Column(length = 250, nullable = false)
    var formaBasica: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(nullable = false)
    var traducao: String
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Vocabulario, String> {

    override fun merge(source: Vocabulario) {
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.traducao = source.traducao
    }

    override fun getId(): String {
        return vocabulario
    }

    override fun create(id: String): Vocabulario {
        return Vocabulario(id, "", "", "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vocabulario

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
