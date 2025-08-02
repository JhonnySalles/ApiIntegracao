package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class MangaCapitulo(
    private var id: UUID? = null,
    @Expose var manga: String = "",
    @Expose var volume: Int = 0,
    @Expose var capitulo: Float = 0f,
    @Expose var lingua: Linguagens = Linguagens.PORTUGUESE,
    @Expose var scan: String = "",
    @Expose var paginas: MutableList<MangaPagina> = mutableListOf(),
    @Expose var isExtra: Boolean = false,
    @Expose var isRaw: Boolean = false,
    @Expose var vocabularios: MutableSet<MangaVocabulario> = mutableSetOf(),
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, MangaCapitulo>() {

    companion object : EntityFactory<UUID?, MangaCapitulo> {
        override fun create(id: UUID?): MangaCapitulo = MangaCapitulo(id, "", 0, 0f, Linguagens.PORTUGUESE, "", mutableListOf(), isExtra = false, isRaw = false, mutableSetOf(), LocalDateTime.now())
    }

    override fun merge(source: MangaCapitulo) {
        this.manga = source.manga
        this.volume = source.volume
        this.capitulo = source.capitulo
        this.lingua = source.lingua
        this.scan = source.scan
        this.isExtra = source.isExtra
        this.isRaw = source.isRaw
        this.vocabularios = source.vocabularios
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: MangaCapitulo) {
        if (source.manga.isNotEmpty())
            this.manga = source.manga

        if (source.volume > 0)
            this.volume = source.volume

        if (source.capitulo > 0)
            this.capitulo = source.capitulo

        if (source.scan.isNotEmpty())
            this.scan = source.scan

        if (source.vocabularios.isNotEmpty())
            this.vocabularios = source.vocabularios

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
        other as MangaCapitulo
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
