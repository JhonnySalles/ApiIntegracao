package br.com.fenix.apiintegracao.model.textoingles

import br.com.fenix.apiintegracao.model.EntityBase
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "revisar")
data class ValidoIngles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private var id: UUID?,
    @Column(length = 250, nullable = false)
    var palavra: String,
    @Column
    var atualizacao: LocalDateTime = LocalDateTime.now()
) : Serializable, EntityBase<ValidoIngles, UUID?>() {

    override fun merge(source: ValidoIngles) {
        this.palavra = source.palavra
    }

    override fun patch(source: ValidoIngles) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): ValidoIngles {
        return ValidoIngles(id, "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValidoIngles

        if (palavra != other.palavra) return false

        return true
    }

    override fun hashCode(): Int {
        return palavra.hashCode()
    }
}
