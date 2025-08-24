package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import java.time.LocalDateTime
import java.util.*

data class NovelVolumeDto(
    private var id: UUID?,
    var novel: String,
    var titulo: String,
    var tituloAlternativo: String,
    var serie: String,
    var descricao: String,
    var arquivo: String,
    var editora: String,
    var autor: String,
    var volume: Float,
    var lingua: Linguagens,
    var favorito: Boolean,
    var processado: Boolean,
    var capa: NovelCapaDto?,
    var capitulos: MutableList<NovelCapituloDto>,
    var vocabularios: MutableSet<NovelVocabularioDto>,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", "","","","","","","",0f, Linguagens.PORTUGUESE, false, false, null, mutableListOf(), mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NovelVolumeDto

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
