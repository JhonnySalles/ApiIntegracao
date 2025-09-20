package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import java.time.LocalDateTime
import java.util.*

data class MangaVocabularioDto(
    private var id: UUID?,
    val palavra: String,
    var leitura: String,
    var ingles: String,
    var portugues: String,
    var revisado: Boolean,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", "", "", "", false, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaVocabularioDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
