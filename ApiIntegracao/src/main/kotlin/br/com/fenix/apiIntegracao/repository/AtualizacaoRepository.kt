package br.com.fenix.apiIntegracao.repository

import br.com.fenix.apiIntegracao.model.Atualizacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AtualizacaoRepository : JpaRepository<Atualizacao, String> {

    @Query(value = "SELECT c FROM Atualizacao c WHERE c.computador = ?1")
    fun findByComputador(computador: String): Optional<Atualizacao>

}