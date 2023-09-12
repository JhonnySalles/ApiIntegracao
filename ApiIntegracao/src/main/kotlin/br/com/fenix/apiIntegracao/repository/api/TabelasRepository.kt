package br.com.fenix.apiIntegracao.repository.api

import br.com.fenix.apiIntegracao.enums.Tipo
import br.com.fenix.apiIntegracao.model.api.Tabelas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TabelasRepository : JpaRepository<Tabelas, String> {

    @Query(value = "SELECT t FROM Tabelas t WHERE t.tipo = ?1")
    fun findByBase(base: Tipo): Optional<Tabelas>
}