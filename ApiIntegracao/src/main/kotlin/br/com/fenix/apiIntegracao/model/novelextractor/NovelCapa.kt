package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import java.awt.image.BufferedImage
import java.io.Serializable
import java.util.*

data class NovelCapa(
    private var id: UUID? = null,
    var manga: String = "",
    var volume: Int = 0,
    var lingua: Linguagens = Linguagens.PORTUGUESE,
    var arquivo: String = "",
    var extenssao: String = "",
    var imagem: BufferedImage? = null
) : Serializable, EntityBase<UUID?, NovelCapa>() {

    override fun merge(source: NovelCapa) {
        this.manga = source.manga
        this.volume = source.volume
        this.lingua = source.lingua
        this.arquivo = source.arquivo
        this.extenssao = source.extenssao
        this.imagem = source.imagem
    }

    override fun patch(source: NovelCapa) {
        if (source.manga.isNotEmpty())
            this.manga = source.manga

        if (source.volume > 0)
            this.volume = source.volume

        if (source.arquivo.isNotEmpty())
            this.arquivo = source.arquivo

        if (source.extenssao.isNotEmpty())
            this.extenssao = source.extenssao

        if (source.imagem != null)
            this.imagem = source.imagem
    }

    override fun getId(): UUID? = id
    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun create(id: UUID?): NovelCapa {
        return NovelCapa(id, "", 0, Linguagens.PORTUGUESE, "", "", null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NovelCapa

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}