package br.com.fenix.apiIntegracao.model.textojapones

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "words_kanji_info")
data class KanjiInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequencia: Long,
    @Column(length = 100, nullable = false)
    var word: String,
    @Column(length = 350, nullable = false)
    var readInfo: String,
    @Column(nullable = false)
    var frequency: Int,
    @Column(nullable = false)
    var tabela: String
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<KanjiInfo, Long> {

    override fun merge(source: KanjiInfo) {
        this.word = source.word
        this.readInfo = source.readInfo
        this.frequency = source.frequency
        this.tabela = source.tabela
    }

    override fun getId(): Long {
        return sequencia
    }

    override fun create(id: Long): KanjiInfo {
        return KanjiInfo(id, "", "", 0, "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KanjiInfo

        if (word != other.word) return false

        return true
    }

    override fun hashCode(): Int {
        return word.hashCode()
    }
}
