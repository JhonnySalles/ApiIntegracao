package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import java.awt.image.BufferedImage
import java.io.Serializable
import java.util.*

data class MangaCapa(
    private var id: UUID? = null,
    var manga: String = "",
    var volume: Int = 0,
    var lingua: Linguagens = Linguagens.PORTUGUESE,
    var arquivo: String = "",
    var extenssao: String = "",
    var imagem: BufferedImage? = null
) : Serializable, EntityBase<UUID?, MangaCapa>() {

    override fun merge(source: MangaCapa) {
        this.manga = source.manga
        this.volume = source.volume
        this.lingua = source.lingua
        this.arquivo = source.arquivo
        this.extenssao = source.extenssao
        this.imagem = source.imagem
    }

    override fun patch(source: MangaCapa) {
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

    override fun create(id: UUID?): MangaCapa {
        return MangaCapa(id, "", 0, Linguagens.PORTUGUESE, "", "", null)
    }

}