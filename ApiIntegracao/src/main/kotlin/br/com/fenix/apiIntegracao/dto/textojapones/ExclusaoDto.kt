package br.com.fenix.apiIntegracao.dto.textojapones

import java.io.Serializable
import java.util.*

data class ExclusaoDto(
    var id: UUID?,
    val exclusao: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExclusaoDto

        if (exclusao != other.exclusao) return false

        return true
    }

    override fun hashCode(): Int {
        return exclusao.hashCode()
    }
}
