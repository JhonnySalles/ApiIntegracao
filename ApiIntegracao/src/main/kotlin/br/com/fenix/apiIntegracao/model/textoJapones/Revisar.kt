package br.com.fenix.apiIntegracao.model.textoJapones

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "revisar")
data class Revisar(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 250, nullable = false)
    val vocabulario: String,
    @Column(length = 250, nullable = false)
    var formaBasica: String,
    @Column(length = 250, nullable = false)
    var leitura: String,
    @Column(nullable = false)
    var traducao: String,
    @Column(nullable = false)
    var ingles: String,
    @Column(nullable = false)
    var revisado: Boolean,
    @Column(nullable = false)
    var aparece: Int,
    @Column(nullable = false)
    var isAnime: Boolean,
    @Column(nullable = false)
    var isManga: Boolean
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Revisar, String> {

    override fun merge(source: Revisar) {
        this.formaBasica = source.formaBasica
        this.leitura = source.leitura
        this.traducao = source.traducao
        this.ingles = source.ingles
        this.revisado = source.revisado
        this.aparece = source.aparece
        this.isAnime = source.isAnime
        this.isManga = source.isManga
    }

    override fun getId(): String {
        return vocabulario
    }

    override fun create(id: String): Revisar {
        return Revisar(id, "", "", "", "", false, 0, isAnime = false, isManga = false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Revisar

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
