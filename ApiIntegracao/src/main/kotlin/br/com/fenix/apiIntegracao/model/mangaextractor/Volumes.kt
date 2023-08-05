package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.enums.Linguagens
import java.io.Serializable


data class Volumes(
    val id: Long,
    var manga: String,
    var volume: Int,
    var linguagem: Linguagens?,
    var arquivo: String,
    var scan: String,
    var isProcessado: Boolean,
    var vocabulario: Set<Vocabulario> = setOf(),
    var capitulos: List<Capitulos> = listOf()
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Volumes, Long> {

    override fun merge(source: Volumes) {
        this.manga = source.manga
        this.volume = source.volume
        this.linguagem = source.linguagem
        this.scan = source.scan
        this.arquivo = source.arquivo
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): Long {
        return id
    }

    override fun create(id: Long): Volumes {
        return Volumes(id, "", 0, Linguagens.PORTUGUESE, "", "",false, setOf(), listOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Volumes

        if (id != other.id) return false
        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (linguagem != other.linguagem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + linguagem.hashCode()
        return result
    }
}
