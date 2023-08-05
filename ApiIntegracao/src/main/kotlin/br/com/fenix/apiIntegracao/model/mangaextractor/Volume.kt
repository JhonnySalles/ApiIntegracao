package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.enums.Linguagens
import br.com.fenix.apiIntegracao.model.Entity
import java.io.Serializable


data class Volume(
    private val id: Long?,
    var manga: String,
    var volume: Int,
    var linguagem: Linguagens?,
    var arquivo: String,
    var scan: String,
    var isProcessado: Boolean,
    var capitulos: List<Capitulo> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, Entity<Volume, Long?> {

    override fun merge(source: Volume) {
        this.manga = source.manga
        this.volume = source.volume
        this.linguagem = source.linguagem
        this.scan = source.scan
        this.arquivo = source.arquivo
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): Long? {
        return id
    }

    override fun create(id: Long?): Volume {
        return Volume(id, "", 0, Linguagens.PORTUGUESE, "", "",false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Volume

        if (id != other.id) return false
        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (linguagem != other.linguagem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + linguagem.hashCode()
        return result
    }
}
