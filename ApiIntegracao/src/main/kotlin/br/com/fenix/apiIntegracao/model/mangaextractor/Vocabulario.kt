package br.com.fenix.apiIntegracao.model.mangaextractor

import java.io.Serializable

data class Vocabulario(
    val id: Long,
    var palavra: String,
    var portugues: String,
    var ingles: String,
    var leitura: String,
    var isRevisado: Boolean,
    var volumes: Volumes? = null,
    var capitulos: Capitulos? = null,
    var paginas: Paginas? = null
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Vocabulario, Long> {

    override fun merge(source: Vocabulario) {
        this.palavra = source.palavra
        this.portugues = source.portugues
        this.ingles = source.ingles
        this.leitura = source.leitura
        this.isRevisado = source.isRevisado
        this.volumes = source.volumes
        this.capitulos = source.capitulos
        this.paginas = source.paginas
    }

    override fun getId(): Long {
        return id
    }

    override fun create(id: Long): Vocabulario {
        return Vocabulario(id, "", "", "", "", false, null, null, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vocabulario

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}