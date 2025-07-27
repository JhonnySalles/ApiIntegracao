package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import java.io.Serializable
import java.util.*


data class Volume(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var linguagem: Linguagens?,
    var arquivo: String,
    var scan: String,
    var isProcessado: Boolean,
    var capitulos: List<Capitulo> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, EntityBase<UUID?, Volume>() {

    override fun merge(source: Volume) {
        this.manga = source.manga
        this.volume = source.volume
        this.linguagem = source.linguagem
        this.scan = source.scan
        this.arquivo = source.arquivo
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun patch(source: Volume) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): Volume {
        return Volume(id, "", 0, Linguagens.PORTUGUESE, "", "",false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Volume

        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (linguagem != other.linguagem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + (linguagem?.hashCode() ?: 0)
        return result
    }

}
