package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.mangaextractor.MangaPagina
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVocabulario
import br.com.fenix.apiintegracao.model.novelextractor.NovelTexto
import br.com.fenix.apiintegracao.model.novelextractor.NovelVocabulario
import com.google.gson.annotations.Expose
import java.time.LocalDateTime
import java.util.*

data class NovelCapituloDto(
    private var id: UUID?,
    var novel: String,
    var volume: Float,
    var capitulo: Float,
    var descricao: String,
    var sequencia: Int,
    var lingua: Linguagens,
    var textos: MutableList<NovelTexto>,
    var vocabularios: MutableSet<NovelVocabulario>,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null, "", 0f,0f,"",0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NovelCapituloDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
