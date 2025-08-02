package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.dto.novelextractor.NovelVolumeDto
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapa
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapitulo
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVocabulario
import java.time.LocalDateTime
import java.util.*

data class MangaVolumeDto(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var lingua: Linguagens,
    var capitulos: MutableList<MangaCapitulo>,
    var vocabularios: MutableSet<MangaVocabulario>,
    var arquivo: String,
    var processado: Boolean,
    var capa: MangaCapa?,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(),"", false, null, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MangaVolumeDto

        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (lingua != other.lingua) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + lingua.hashCode()
        return result
    }

}
