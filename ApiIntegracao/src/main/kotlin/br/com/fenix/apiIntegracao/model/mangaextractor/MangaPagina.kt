package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.model.EntityBase
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.*

data class MangaPagina(
    private var id: UUID? = null,
    @Expose var nomePagina: String = "",
    @Expose var numero: Int = 0,
    @Expose var hash: String = "",
    @Expose var textos: MutableList<MangaTexto> = mutableListOf(),
    @Expose var vocabularios: MutableSet<MangaVocabulario> = mutableSetOf(),
    @Expose var sequencia: Int = 0
) : Serializable, EntityBase<UUID?, MangaPagina>() {

    override fun merge(source: MangaPagina) {
        this.nomePagina = source.nomePagina
        this.numero = source.numero
        this.hash = source.hash
        this.textos = source.textos
        this.vocabularios = source.vocabularios
        this.sequencia = source.sequencia
    }

    override fun patch(source: MangaPagina) {
        if (source.nomePagina.isNotEmpty())
            this.nomePagina = source.nomePagina

        if (source.sequencia > 0)
            this.numero = source.numero

        if (source.hash.isNotEmpty())
            this.hash = source.hash

        if (source.textos.isNotEmpty())
            this.textos = source.textos

        if (source.vocabularios.isNotEmpty())
            this.vocabularios = source.vocabularios

        if (source.sequencia > 0)
            this.sequencia = source.sequencia
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): MangaPagina {
        return MangaPagina(id, "", 0, "", mutableListOf(), mutableSetOf(), 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaPagina
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
