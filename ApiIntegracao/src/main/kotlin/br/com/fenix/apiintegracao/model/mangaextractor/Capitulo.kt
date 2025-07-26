package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import java.io.Serializable
import java.util.*

data class Capitulo(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var capitulo: Double,
    var linguagem: Linguagens?,
    var scan: String,
    var isExtra: Boolean,
    var isRaw: Boolean,
    var isProcessado: Boolean,
    var paginas: List<Pagina> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, EntityBase<Capitulo, UUID?>() {

    override fun merge(source: Capitulo) {
        this.manga = source.manga
        this.volume = source.volume
        this.capitulo = source.capitulo
        this.linguagem = source.linguagem
        this.scan = source.scan
        this.isExtra = source.isExtra
        this.isRaw = source.isRaw
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun patch(source: Capitulo) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): Capitulo {
        return Capitulo(id, "", 0, 0.0, Linguagens.PORTUGUESE, "", false, false, false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Capitulo

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
