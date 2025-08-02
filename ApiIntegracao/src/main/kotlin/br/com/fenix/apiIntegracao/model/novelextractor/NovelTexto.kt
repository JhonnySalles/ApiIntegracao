package br.com.fenix.apiintegracao.model.novelextractor

import br.com.fenix.apiintegracao.model.EntityBase
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.*

data class NovelTexto(
    private var id: UUID? = null,
    @Expose var texto: String = "",
    @Expose var sequencia: Int = 0
) : Serializable, EntityBase<UUID?, NovelTexto>() {

    override fun merge(source: NovelTexto) {
        this.sequencia = source.sequencia
        this.texto = source.texto
    }

    override fun patch(source: NovelTexto) {
        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.texto.isNotEmpty())
            this.texto = source.texto
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): NovelTexto {
        return NovelTexto(id, "", 0)
    }

    override fun toString(): String {
        return "MangaTexto [id=$id, texto=$texto, sequencia=$sequencia]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NovelTexto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
