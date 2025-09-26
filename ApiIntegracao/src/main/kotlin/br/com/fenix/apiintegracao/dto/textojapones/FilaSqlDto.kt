package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class FilaSqlDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var sequencial: Long,
    @JsonView(Views.Detail::class)
    var selectSQL: String,
    @JsonView(Views.Detail::class)
    var updateSQL: String,
    @JsonView(Views.Detail::class)
    var deleteSQL: String,
    @JsonView(Views.Detail::class)
    var vocabulario: String,
    @JsonView(Views.Detail::class)
    var isExporta: Boolean,
    @JsonView(Views.Detail::class)
    var isLimpeza: Boolean,
    @JsonView(Views.Summary::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, 0, "","","","", false, false)

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilaSqlDto

        if (sequencial != other.sequencial) return false

        return true
    }

    override fun hashCode(): Int {
        return sequencial.hashCode()
    }
}
