package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import java.util.*

data class VocabularioDto(
    private var id: UUID?,
    var palavra: String,
    var portugues: String,
    var ingles: String,
    var leitura: String,
    var isRevisado: Boolean,
    var volumes: VolumeDto? = null,
    var capitulos: CapituloDto? = null,
    var paginas: PaginaDto? = null
) : DtoBase<UUID?>() {

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VocabularioDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
