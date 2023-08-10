package br.com.fenix.apiIntegracao.dto.textojapones

import java.io.Serializable
import java.util.*

data class KanjiInfoDto(
    var id: UUID?,
    var sequencia: Long,
    var word: String,
    var readInfo: String,
    var frequency: Int,
    var tabela: String
) : Serializable {

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
