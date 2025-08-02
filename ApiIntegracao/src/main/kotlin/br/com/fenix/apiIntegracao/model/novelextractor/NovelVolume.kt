package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class NovelVolume(
    private var id: UUID? = null,
    @Expose var novel: String = "",
    @Expose var titulo: String = "",
    @Expose var tituloAlternativo: String = "",
    @Expose var serie: String = "",
    @Expose var descricao: String = "",
    @Expose var arquivo: String = "",
    @Expose var editora: String = "",
    @Expose var autor: String = "",
    @Expose var volume: Float = 0f,
    @Expose var lingua: Linguagens = Linguagens.PORTUGUESE,
    var favorito: Boolean = false,
    var processado: Boolean = false,
    var capa: NovelCapa? = null,
    @Expose var capitulos: MutableList<NovelCapitulo> = mutableListOf(),
    @Expose var vocabularios: MutableSet<NovelVocabulario> = mutableSetOf(),
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, NovelVolume>() {

    companion object : EntityFactory<UUID?, NovelVolume> {
        override fun create(id: UUID?): NovelVolume = NovelVolume(id, "", "", "", "", "", "", "", "", 0f, Linguagens.PORTUGUESE, false, false, null, mutableListOf(), mutableSetOf(), LocalDateTime.now())
    }

    override fun merge(source: NovelVolume) {
        this.novel = source.novel
        this.titulo = source.titulo
        this.tituloAlternativo = source.tituloAlternativo
        this.descricao = source.descricao
        this.serie = source.serie
        this.editora = source.editora
        this.autor = source.autor
        this.volume = source.volume
        this.lingua = source.lingua
        this.capitulos = source.capitulos
        this.vocabularios = source.vocabularios
        this.arquivo = source.arquivo
        this.capa = source.capa
        this.processado = source.processado
        this.favorito = source.favorito
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: NovelVolume) {
        if (source.novel.isNotEmpty())
            this.novel = source.novel

        if (source.titulo.isNotEmpty())
            this.titulo = source.titulo

        if (source.tituloAlternativo.isNotEmpty())
            this.tituloAlternativo = source.tituloAlternativo

        if (source.descricao.isNotEmpty())
            this.descricao = source.descricao

        if (source.serie.isNotEmpty())
            this.serie = source.serie

        if (source.editora.isNotEmpty())
            this.editora = source.editora

        if (source.autor.isNotEmpty())
            this.autor = source.autor

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

        other as NovelVolume

        if (novel != other.novel) return false
        if (volume != other.volume) return false
        if (lingua != other.lingua) return false

        return true
    }

    override fun hashCode(): Int {
        var result = novel.hashCode()
        result = 31 * result + volume.hashCode()
        result = 31 * result + lingua.hashCode()
        return result
    }

}
