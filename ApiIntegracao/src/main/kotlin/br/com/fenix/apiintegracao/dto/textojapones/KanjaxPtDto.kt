package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*

data class KanjaxPtDto(
    private var id: UUID?,
    var sequencia: Long,
    var kanji: String,
    var keyword: String,
    var meaning: String,
    var koohii1: String,
    var koohii2: String,
    var onyomi: String,
    var kunyomi: String,
    var onwords: String,
    var kunwords: String,
    var jlpt: Int,
    var grade: Int,
    var freq: Int,
    var strokes: Int,
    var variants: String,
    var radical: String,
    var parts: String,
    var utf8: String,
    var sjis: String,
    var isTraduzido: Boolean,
    var isChecado: Boolean,
    var isRevisado: Boolean,
    var isSinaliza: Boolean,
    var dataTraducao: LocalDateTime,
    var dataCorrecao: LocalDateTime,
    var observacao: String,
    var kanjaxOriginal: Boolean,
    var palavra: String,
    var significado: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : DtoBase<UUID?>() {

    constructor(): this(null, 0,"","", "","","","","","","",0,0,0,0,"","","","","",false,false,false,false, LocalDateTime.MIN,LocalDateTime.MIN,"",false, "", "")

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KanjaxPtDto

        if (kanji != other.kanji) return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
