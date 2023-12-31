package br.com.fenix.apiIntegracao.repository.api

import br.com.fenix.apiIntegracao.model.api.Consultas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AtualizacaoRepository : JpaRepository<Consultas, String> {

    @Query(value = "SELECT c FROM Consultas c WHERE c.computador = ?1")
    fun findByComputador(computador: String): Optional<Consultas>
}