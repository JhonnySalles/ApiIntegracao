package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class KanjiInfoDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var sequencia: Long,
    @JsonView(Views.Detail::class)
    var word: String,
    @JsonView(Views.Detail::class)
    var readInfo: String,
    @JsonView(Views.Detail::class)
    var frequency: Int,
    @JsonView(Views.Detail::class)
    var tabela: String,
    @JsonView(Views.Summary::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, 0, "","",0,"")

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KanjiInfoDto

        if (word != other.word) return false

        return true
    }

    override fun hashCode(): Int {
        return word.hashCode()
    }
}
