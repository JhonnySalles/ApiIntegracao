package br.com.fenix.apiIntegracao.model.textojapones

import br.com.fenix.apiIntegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "fila_sql")
data class FilaSqlJapones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column
    var sequencial: Long,
    @Column(nullable = false)
    var selectSQL: String,
    @Column(nullable = false)
    var updateSQL: String,
    @Column(nullable = false)
    var deleteSQL: String,
    @Column(nullable = false)
    var vocabulario: String,
    @Column(nullable = false)
    var isExporta: Boolean,
    @Column(nullable = false)
    var isLimpeza: Boolean,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<FilaSqlJapones, UUID?>() {

    override fun merge(source: FilaSqlJapones) {
        this.sequencial = source.sequencial
        this.selectSQL = source.selectSQL
        this.updateSQL = source.updateSQL
        this.deleteSQL = source.deleteSQL
        this.vocabulario = source.vocabulario
        this.isExporta = source.isExporta
        this.isLimpeza = source.isLimpeza
    }

    override fun patch(source: FilaSqlJapones) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): FilaSqlJapones {
        return FilaSqlJapones(id, 0, "", "", "", "", false, false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilaSqlJapones

        if (sequencial != other.sequencial) return false

        return true
    }

    override fun hashCode(): Int {
        return sequencial.hashCode()
    }
}
