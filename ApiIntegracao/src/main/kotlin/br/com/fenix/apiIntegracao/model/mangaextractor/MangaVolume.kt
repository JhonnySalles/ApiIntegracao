package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class MangaVolume(
    private var id: UUID? = null,
    @Expose var manga: String = "",
    @Expose var volume: Int = 0,
    @Expose var lingua: Linguagens = Linguagens.PORTUGUESE,
    @Expose var capitulos: MutableList<MangaCapitulo> = mutableListOf(),
    @Expose var vocabularios: MutableSet<MangaVocabulario> = mutableSetOf(),
    @Expose var arquivo: String = "",
    var processado: Boolean = false,
    var capa: MangaCapa? = null,
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, MangaVolume>() {

    companion object : EntityFactory<UUID?, MangaVolume> {
        override fun create(id: UUID?): MangaVolume = MangaVolume(id, "", 0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(), "", false, null, LocalDateTime.now())
    }

    override fun merge(source: MangaVolume) {
        this.manga = source.manga
        this.volume = source.volume
        this.lingua = source.lingua
        this.capitulos = source.capitulos
        this.vocabularios = source.vocabularios
        this.arquivo = source.arquivo
        this.processado = source.processado
        this.capa = source.capa
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: MangaVolume) {
        if (source.manga.isNotEmpty())
            this.manga = source.manga

        if (source.volume > 0)
            this.volume = source.volume

        if (source.capitulos.isNotEmpty())
            this.capitulos = source.capitulos

        if (source.vocabularios.isNotEmpty())
            this.vocabularios = source.vocabularios

        if (source.arquivo.isNotEmpty())
            this.arquivo = source.arquivo

        if (source.capa != null)
            this.capa = source.capa

        if (source.atualizacao != null)
            this.atualizacao = source.atualizacao
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MangaVolume
        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (lingua != other.lingua) return false
        return true
    }

    override fun hashCode(): Int {
        var result = manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + lingua.hashCode()
        return result
    }

}
