package br.com.fenix.apiintegracao.model.textojapones

import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "kanjax_pt")
data class KanjaxPt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
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
    @Column(name = "isTraduzido")
    var isTraduzido: Boolean,
    @Column(name = "isChecado")
    var isChecado: Boolean,
    @Column(name = "isRevisado")
    var isRevisado: Boolean,
    @Column(name = "sinaliza")
    var isSinaliza: Boolean,
    @Column(name = "data_traducao")
    var dataTraducao: LocalDateTime?,
    @Column(name = "data_correcao")
    var dataCorrecao: LocalDateTime?,
    @Column(name = "obs", length = 100)
    var observacao: String?,
    @Column(name = "isKanjax_original", nullable = false)
    var kanjaxOriginal: Boolean,
    @Column(length = 100, nullable = false)
    var palavra: String,
    @Column(length = 250, nullable = false)
    var significado: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<UUID?, KanjaxPt>() {

    companion object : EntityFactory<UUID?, KanjaxPt> {
        override fun create(id: UUID?): KanjaxPt = KanjaxPt(
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

    override fun patch(source: KanjaxPt) {
        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.kanji.isNotEmpty())
            this.kanji = source.kanji

        if (source.keyword.isNotEmpty())
            this.keyword = source.keyword

        if (source.meaning.isNotEmpty())
            this.meaning = source.meaning

        if (source.koohii1.isNotEmpty())
            this.koohii1 = source.koohii1

        if (source.koohii2.isNotEmpty())
            this.koohii2 = source.koohii2

        if (source.onyomi.isNotEmpty())
            this.onyomi = source.onyomi

        if (source.kunyomi.isNotEmpty())
            this.kunyomi = source.kunyomi

        if (source.onwords.isNotEmpty())
            this.onwords = source.onwords

        if (source.kunwords.isNotEmpty())
            this.kunwords = source.kunwords

        if (source.jlpt > 0)
            this.jlpt = source.jlpt

        if (source.grade > 0)
            this.grade = source.grade

        if (source.freq > 0)
            this.freq = source.freq

        if (source.strokes > 0)
            this.strokes = source.strokes

        if (source.variants.isNotEmpty())
            this.variants = source.variants

        if (source.radical.isNotEmpty())
            this.radical = source.radical

        if (source.parts.isNotEmpty())
            this.parts = source.parts

        if (source.utf8.isNotEmpty())
            this.utf8 = source.utf8

        if (source.sjis.isNotEmpty())
            this.sjis = source.sjis

        if (source.observacao != null && source.observacao!!.isNotEmpty())
            this.observacao = source.observacao

        if (source.palavra.isNotEmpty())
            this.palavra = source.palavra

        if (source.significado.isNotEmpty())
            this.significado = source.significado
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as KanjaxPt

        if (kanji != other.kanji)
            return false

        return true
    }

    override fun hashCode(): Int {
        return kanji.hashCode()
    }
}
