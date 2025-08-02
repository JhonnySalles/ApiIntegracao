package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.model.EntityBase
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.*

data class MangaTexto(
    private var id: UUID? = null,
    @Expose var texto: String = "",
    @Expose var sequencia: Int = 0,
    @Expose var x1: Int = 0,
    @Expose var y1: Int = 0,
    @Expose var x2: Int = 0,
    @Expose var y2: Int = 0
) : Serializable, EntityBase<UUID?, MangaTexto>() {

    override fun merge(source: MangaTexto) {
        this.sequencia = source.sequencia
        this.texto = source.texto
        this.x1 = source.x1
        this.y1 = source.y1
        this.x2 = source.x2
        this.y2 = source.y2
    }

    override fun patch(source: MangaTexto) {
        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.texto.isNotEmpty())
            this.texto = source.texto

        if (source.x1 > 0)
            this.x1 = source.x1

        if (source.y1 > 0)
            this.y1 = source.y1

        if (source.x2 > 0)
            this.x2 = source.x2

        if (source.y2 > 0)
            this.y2 = source.y2
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): MangaTexto {
        return MangaTexto(id, "", 0, 0, 0, 0, 0)
    }

    override fun toString(): String {
        return "MangaTexto [id=$id, texto=$texto, sequencia=$sequencia]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaTexto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
