package br.com.fenix.apiIntegracao.model.textojapones

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "kanjax_pt")
data class KanjaxPt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: String?,
    @Column(name = "Sequencia", nullable = false)
    var sequencia: Long,
    @Column(length = 10, nullable = false)
    var kanji: String,
    @Column(length = 100, nullable = false)
    var keyword: String,
    @Column(length = 250, nullable = false)
    var meaning: String,
    @Column(nullable = false)
    var koohii1: String,
    @Column(nullable = false)
    var koohii2: String,
    @Column(length = 100, nullable = false)
    var onyomi: String,
    @Column(length = 100, nullable = false)
    var kunyomi: String,
    @Column(nullable = false)
    var onwords: String,
    @Column(nullable = false)
    var kunwords: String,
    @Column(nullable = false)
    var jlpt: Int,
    @Column(nullable = false)
    var grade: Int,
    @Column(nullable = false)
    var freq: Int,
    @Column(nullable = false)
    var strokes: Int,
    @Column(length = 100, nullable = false)
    var variants: String,
    @Column(length = 100, nullable = false)
    var radical: String,
    @Column(length = 100, nullable = false)
    var parts: String,
    @Column(length = 10, nullable = false)
    var utf8: String,
    @Column(length = 5, nullable = false)
    var sjis: String,
    @Column(name = "traduzido")
    var isTraduzido: Boolean,
    @Column(name = "checado")
    var isChecado: Boolean,
    @Column(name = "revisado")
    var isRevisado: Boolean,
    @Column(name = "sinalizado")
    var isSinaliza: Boolean,
    @Column(name = "data_traducao")
    var dataTraducao: LocalDateTime,
    @Column(name = "data_correcao")
    var dataCorrecao: LocalDateTime,
    @Column(name = "obs", length = 100)
    var observacao: String,
    @Column(nullable = false)
    var kanjaxOriginal: Boolean,
    @Column(length = 100, nullable = false)
    var palavra: String,
    @Column(length = 250, nullable = false)
    var significado: String
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<KanjaxPt, String?> {

    override fun merge(source: KanjaxPt) {
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

    override fun create(id: String?): KanjaxPt {
        return KanjaxPt(
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

        other as KanjaxPt

        if (kanji != other.kanji) return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
