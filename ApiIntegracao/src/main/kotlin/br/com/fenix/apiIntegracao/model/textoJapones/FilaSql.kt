package br.com.fenix.apiIntegracao.model.textojapones

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "fila_sql")
data class FilaSql(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequencial: Long,
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
    var isLimpeza: Boolean
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<FilaSql, Long> {

    override fun merge(source: FilaSql) {
        this.selectSQL = source.selectSQL
        this.updateSQL = source.updateSQL
        this.deleteSQL = source.deleteSQL
        this.vocabulario = source.vocabulario
        this.isExporta = source.isExporta
        this.isLimpeza = source.isLimpeza
    }

    override fun getId(): Long {
        return sequencial
    }

    override fun create(id: Long): FilaSql {
        return FilaSql(id, "", "", "", "", false, false)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilaSql

        if (sequencial != other.sequencial) return false

        return true
    }

    override fun hashCode(): Int {
        return sequencial.hashCode()
    }
}
