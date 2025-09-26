package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class VocabularioDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    val vocabulario: String,
    @JsonView(Views.Detail::class)
    var formaBasica: String,
    @JsonView(Views.Detail::class)
    var leitura: String,
    @JsonView(Views.Detail::class)
    var leituraNovel: String,
    @JsonView(Views.Detail::class)
    var ingles: String,
    @JsonView(Views.Detail::class)
    var portugues: String,
    @JsonView(Views.Detail::class)
    var jlpt: Int,
    @JsonView(Views.Summary::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, "", "","","","", "", 0)

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
        return vocabulario == other.vocabulario
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
