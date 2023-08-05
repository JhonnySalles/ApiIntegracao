package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.enums.Linguagens
import br.com.fenix.apiIntegracao.model.Entity
import java.io.Serializable

data class Capitulo(
    private val id: Long?,
    var manga: String,
    var volume: Int,
    var capitulo: Double,
    var linguagem: Linguagens?,
    var scan: String,
    var isExtra: Boolean,
    var isRaw: Boolean,
    var isProcessado: Boolean,
    var paginas: List<Pagina> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, Entity<Capitulo, Long?> {

    override fun merge(source: Capitulo) {
        this.manga = source.manga
        this.volume = source.volume
        this.capitulo = source.capitulo
        this.linguagem = source.linguagem
        this.scan = source.scan
        this.isExtra = source.isExtra
        this.isRaw = source.isRaw
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): Long? {
        return id
    }

    override fun create(id: Long?): Capitulo {
        return Capitulo(id, "", 0, 0.0, Linguagens.PORTUGUESE, "", false, false, false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Capitulo

        if (id != other.id) return false
        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (capitulo != other.capitulo) return false
        if (linguagem != other.linguagem) return false
        if (scan != other.scan) return false
        if (isExtra != other.isExtra) return false
        if (isRaw != other.isRaw) return false
        if (isProcessado != other.isProcessado) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + capitulo.hashCode()
        result = 31 * result + linguagem.hashCode()
        result = 31 * result + scan.hashCode()
        result = 31 * result + isExtra.hashCode()
        result = 31 * result + isRaw.hashCode()
        result = 31 * result + isProcessado.hashCode()
        return result
    }

}
