package br.com.fenix.apiIntegracao.dto.textojapones

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class EstatisticaDto(
    var id: UUID?,
    var sequencial: Long?,
    var kanji: String,
    var leitura: String,
    var tipo: String,
    var quantidade: Double,
    var percentual: Float,
    var media: Double,
    var percMedia: Float,
    var corSequencial: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable {

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
