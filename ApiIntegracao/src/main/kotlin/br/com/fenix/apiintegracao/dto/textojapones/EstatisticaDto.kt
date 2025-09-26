package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class EstatisticaDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var sequencial: Long?,
    @JsonView(Views.Detail::class)
    var kanji: String,
    @JsonView(Views.Detail::class)
    var leitura: String,
    @JsonView(Views.Detail::class)
    var tipo: String,
    @JsonView(Views.Detail::class)
    var quantidade: Double,
    @JsonView(Views.Detail::class)
    var percentual: Float,
    @JsonView(Views.Detail::class)
    var media: Double,
    @JsonView(Views.Detail::class)
    var percentualMedio: Float,
    @JsonView(Views.Detail::class)
    var corSequencial: Int,
    @JsonView(Views.Summary::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, 0, "","","",0.0,0F,0.0,0F,0)

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EstatisticaDto

        if (kanji != other.kanji) return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}


