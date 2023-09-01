package br.com.fenix.apiIntegracao.dto.textojapones

import br.com.fenix.apiIntegracao.dto.DtoBase
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*

data class RevisarDto(
    private var id: UUID?,
    var vocabulario: String,
    var formaBasica: String,
    var leitura: String,
    var traducao: String,
    var ingles: String,
    var revisado: Boolean,
    var aparece: Int,
    var isAnime: Boolean,
    var isManga: Boolean,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    override fun getId(): UUID? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RevisarDto

        if (vocabulario != other.vocabulario) return false

        return true
    }

    override fun hashCode(): Int {
        return vocabulario.hashCode()
    }
}
