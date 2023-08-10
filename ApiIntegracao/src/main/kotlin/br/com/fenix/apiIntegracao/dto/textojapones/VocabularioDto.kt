package br.com.fenix.apiIntegracao.dto.textojapones

import java.io.Serializable
import java.util.*

data class VocabularioDto(
    var id: UUID?,
    val vocabulario: String,
    var formaBasica: String,
    var leitura: String,
    var traducao: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VocabularioDto

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
