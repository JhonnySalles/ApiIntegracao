package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class NovelTexto(
    private var id: UUID? = null,
    @Expose var texto: String = "",
    @Expose var sequencia: Int = 0,
    var atualizacao: LocalDateTime? = null
) : Serializable, EntityBase<UUID?, NovelTexto>() {

    companion object : EntityFactory<UUID?, NovelTexto> {
        override fun create(id: UUID?): NovelTexto = NovelTexto(id, "", 0, LocalDateTime.now())
    }

    override fun merge(source: NovelTexto) {
        this.sequencia = source.sequencia
        this.texto = source.texto
        this.atualizacao = source.atualizacao
    }

    override fun patch(source: NovelTexto) {
        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.texto.isNotEmpty())
            this.texto = source.texto

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
        other as NovelTexto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
