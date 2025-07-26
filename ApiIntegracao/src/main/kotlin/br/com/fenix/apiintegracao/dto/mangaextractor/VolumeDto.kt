package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import java.util.*


data class VolumeDto(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var linguagem: Linguagens?,
    var arquivo: String,
    var scan: String,
    var isProcessado: Boolean,
    var capitulos: List<CapituloDto> = listOf(),
    var vocabulario: Set<VocabularioDto> = setOf()
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0,null,"","",false)

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VolumeDto

        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (linguagem != other.linguagem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + (linguagem?.hashCode() ?: 0)
        return result
    }

}
