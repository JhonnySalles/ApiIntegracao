package br.com.fenix.apiIntegracao.dto.textojapones

import java.io.Serializable
import java.time.LocalDateTime

data class KanjaxPtDto(
    private var id: String?,
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
    var significado: String
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<KanjaxPtDto, String?> {

    override fun merge(source: KanjaxPtDto) {
        this.sequencia = source.sequencia
        this.kanji = source.kanji
        this.keyword = source.keyword
        this.meaning = source.meaning
        this.koohii1 = source.koohii1
        this.koohii2 = source.koohii2
        this.onyomi = source.onyomi
        this.kunyomi = source.kunyomi
        this.onwords = source.onwords
        this.kunwords = source.kunwords
        this.jlpt = source.jlpt
        this.grade = source.grade
        this.freq = source.freq
        this.strokes = source.strokes
        this.variants = source.variants
        this.radical = source.radical
        this.parts = source.parts
        this.utf8 = source.utf8
        this.sjis = source.sjis
        this.isTraduzido = source.isTraduzido
        this.isChecado = source.isChecado
        this.isRevisado = source.isRevisado
        this.isSinaliza = source.isSinaliza
        this.dataTraducao = source.dataTraducao
        this.dataCorrecao = source.dataCorrecao
        this.observacao = source.observacao
        this.kanjaxOriginal = source.kanjaxOriginal
        this.palavra = source.palavra
        this.significado = source.significado
    }

    override fun getId(): String? {
        return id
    }

    override fun create(id: String?): KanjaxPtDto {
        return KanjaxPtDto(
            id,
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            0,
            0,
            0,
            "",
            "",
            "",
            "",
            "",
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "",
            false,
            "",
            ""
        )
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
