package br.com.fenix.apiIntegracao.dto.textojapones

import java.io.Serializable
import java.util.*

data class FilaSqlDto(
    var id: UUID?,
    var sequencial: Long,
    var selectSQL: String,
    var updateSQL: String,
    var deleteSQL: String,
    var vocabulario: String,
    var isExporta: Boolean,
    var isLimpeza: Boolean
) : Serializable {

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
