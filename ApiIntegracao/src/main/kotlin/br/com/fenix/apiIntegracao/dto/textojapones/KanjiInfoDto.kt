package br.com.fenix.apiIntegracao.dto.textojapones

import br.com.fenix.apiIntegracao.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*

data class KanjiInfoDto(
    private var id: UUID?,
    var sequencia: Long,
    var word: String,
    var readInfo: String,
    var frequency: Int,
    var tabela: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : BaseDto<UUID?>() {

    override fun getId(): UUID? {
        return id
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
