package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "words_kanji_info")
data class KanjiInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column
    var sequencia: Long,
    @Column(length = 100, nullable = false)
    var word: String,
    @Column(length = 350, nullable = false)
    var readInfo: String,
    @Column(nullable = false)
    var frequency: Int,
    @Column(nullable = false)
    var tabela: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<KanjiInfo, UUID?>() {

    override fun merge(source: KanjiInfo) {
        this.sequencia = source.sequencia
        this.word = source.word
        this.readInfo = source.readInfo
        this.frequency = source.frequency
        this.tabela = source.tabela
    }

    override fun patch(source: KanjiInfo) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): KanjiInfo {
        return KanjiInfo(id, 0, "", "", 0, "")
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
