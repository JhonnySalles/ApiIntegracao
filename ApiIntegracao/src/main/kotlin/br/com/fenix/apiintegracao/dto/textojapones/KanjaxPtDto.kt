package br.com.fenix.apiintegracao.dto.textojapones

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class KanjaxPtDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var sequencia: Long,
    @JsonView(Views.Detail::class)
    var kanji: String,
    @JsonView(Views.Detail::class)
    var keyword: String,
    @JsonView(Views.Detail::class)
    var meaning: String,
    @JsonView(Views.Detail::class)
    var koohii1: String,
    @JsonView(Views.Detail::class)
    var koohii2: String,
    @JsonView(Views.Detail::class)
    var onyomi: String,
    @JsonView(Views.Detail::class)
    var kunyomi: String,
    @JsonView(Views.Detail::class)
    var onwords: String,
    @JsonView(Views.Detail::class)
    var kunwords: String,
    @JsonView(Views.Detail::class)
    var jlpt: Int,
    @JsonView(Views.Detail::class)
    var grade: Int,
    @JsonView(Views.Detail::class)
    var freq: Int,
    @JsonView(Views.Detail::class)
    var strokes: Int,
    @JsonView(Views.Detail::class)
    var variants: String,
    @JsonView(Views.Detail::class)
    var radical: String,
    @JsonView(Views.Detail::class)
    var parts: String,
    @JsonView(Views.Detail::class)
    var utf8: String,
    @JsonView(Views.Detail::class)
    var sjis: String,
    @JsonView(Views.Detail::class)
    var isTraduzido: Boolean,
    @JsonView(Views.Detail::class)
    var isChecado: Boolean,
    @JsonView(Views.Detail::class)
    var isRevisado: Boolean,
    @JsonView(Views.Detail::class)
    var isSinaliza: Boolean,
    @JsonView(Views.Detail::class)
    var dataTraducao: LocalDateTime,
    @JsonView(Views.Detail::class)
    var dataCorrecao: LocalDateTime,
    @JsonView(Views.Detail::class)
    var observacao: String,
    @JsonView(Views.Detail::class)
    var kanjaxOriginal: Boolean,
    @JsonView(Views.Detail::class)
    var palavra: String,
    @JsonView(Views.Detail::class)
    var significado: String,
    @JsonView(Views.Summary::class)
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
