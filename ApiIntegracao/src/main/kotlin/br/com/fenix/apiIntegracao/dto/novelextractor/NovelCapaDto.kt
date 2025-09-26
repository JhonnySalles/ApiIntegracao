package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.validations.IsBase64
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

open class NovelCapaDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var novel: String,
    @JsonView(Views.Detail::class)
    var volume: Int,
    @JsonView(Views.Detail::class)
    var lingua: Linguagens,
    @JsonView(Views.Detail::class)
    var arquivo: String,
    @JsonView(Views.Detail::class)
    var extenssao: String,
    @Schema(
        description = "Imagem da capa no formato Data URL Base64 (ex: data:image/png;base64,...).",
        example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
    )
    @IsBase64(message = "O campo 'imagem' deve ser uma string em formato Base64 válido.")
    @JsonView(Views.Detail::class)
    var imagem: String?,
    @JsonView(Views.Summary::class)
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
        other as NovelCapaDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
