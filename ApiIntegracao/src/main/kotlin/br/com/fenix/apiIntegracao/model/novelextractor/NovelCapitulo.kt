package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapitulo
import br.com.fenix.apiintegracao.model.mangaextractor.MangaPagina
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVocabulario
import br.com.fenix.apiintegracao.model.textojapones.EstatisticaJapones
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class NovelCapitulo(
    private var id: UUID? = null,
    @Expose var novel: String = "",
    @Expose var volume: Float = 0f,
    @Expose var capitulo: Float = 0f,
    @Expose var descricao: String = "",
    @Expose var sequencia: Int = 0,
    @Expose var lingua: Linguagens = Linguagens.PORTUGUESE,
    @Expose var textos: MutableList<NovelTexto> = mutableListOf(),
    @Expose var vocabularios: MutableSet<NovelVocabulario> = mutableSetOf(),
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, NovelCapitulo>() {

    companion object : EntityFactory<UUID?, NovelCapitulo> {
        override fun create(id: UUID?): NovelCapitulo = NovelCapitulo(id, "", 0f, 0f, "", 0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(), LocalDateTime.now())
    }

    override fun merge(source: NovelCapitulo) {
        this.novel = source.novel
        this.volume = source.volume
        this.capitulo = source.capitulo
        this.descricao = source.descricao
        this.lingua = source.lingua
        this.sequencia = source.sequencia
        this.textos = source.textos
        this.vocabularios = source.vocabularios
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: NovelCapitulo) {
        if (source.novel.isNotEmpty())
            this.novel = source.novel

        if (source.volume > 0)
            this.volume = source.volume

        if (source.capitulo > 0)
            this.capitulo = source.capitulo

        if (source.descricao.isNotEmpty())
            this.descricao = source.descricao

        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.textos.isNotEmpty())
            this.textos = source.textos

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
        other as NovelCapitulo
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
