package br.com.fenix.apiIntegracao.dto.textoingles

import br.com.fenix.apiIntegracao.dto.DtoBase
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*

data class ValidoDto(
    private var id: UUID?,
    var palavra: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, "")

    override fun getId(): UUID? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValidoDto

        if (palavra != other.palavra) return false

        return true
    }

    override fun hashCode(): Int {
        return palavra.hashCode()
    }
}
