package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import java.awt.image.BufferedImage
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

open class NovelCapa(
    private var id: UUID? = null,
    var novel: String = "",
    var volume: Int = 0,
    var lingua: Linguagens = Linguagens.PORTUGUESE,
    var arquivo: String = "",
    var extenssao: String = "",
    var imagem: ByteArray? = null,
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, NovelCapa>() {

    companion object : EntityFactory<UUID?, NovelCapa> {
        override fun create(id: UUID?): NovelCapa = NovelCapa(id, "", 0, Linguagens.PORTUGUESE, "", "", null, LocalDateTime.now())
    }

    override fun merge(source: NovelCapa) {
        this.novel = source.novel
        this.volume = source.volume
        this.lingua = source.lingua
        this.arquivo = source.arquivo
        this.extenssao = source.extenssao
        this.imagem = source.imagem
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: NovelCapa) {
        if (source.novel.isNotEmpty())
            this.novel = source.novel

        if (source.volume > 0)
            this.volume = source.volume

        if (source.arquivo.isNotEmpty())
            this.arquivo = source.arquivo

        if (source.extenssao.isNotEmpty())
            this.extenssao = source.extenssao

        if (source.imagem != null)
            this.imagem = source.imagem

        if (source.atualizacao != null)
            this.atualizacao = source.atualizacao
    }

    override fun getId(): UUID? = id

    override fun setId(id: UUID?) {
        this.id = id
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