package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.validations.IsBase64
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

data class MangaCapaDto(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var lingua: Linguagens,
    var arquivo: String,
    var extenssao: String,
    @Schema(
        description = "Imagem da capa no formato Data URL Base64 (ex: data:image/png;base64,...).",
        example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
    )
    @IsBase64(message = "O campo 'imagem' deve ser uma string em formato Base64 válido.")
    var imagem: String?,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null, "", 0,Linguagens.PORTUGUESE,"","",null, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaCapaDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
