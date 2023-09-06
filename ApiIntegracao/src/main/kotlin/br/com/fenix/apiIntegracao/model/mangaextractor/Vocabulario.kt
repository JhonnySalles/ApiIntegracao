package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.model.EntityBase
import java.io.Serializable
import java.util.*

data class Vocabulario(
    private var id: UUID?,
    var palavra: String,
    var portugues: String,
    var ingles: String,
    var leitura: String,
    var isRevisado: Boolean,
    var volumes: Volume? = null,
    var capitulos: Capitulo? = null,
    var paginas: Pagina? = null
) : Serializable, EntityBase<Vocabulario, UUID?>() {

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

    override fun getId(): UUID? {
        return id
    }

    fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): Vocabulario {
        return Vocabulario(id, "", "", "", "", false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vocabulario

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
