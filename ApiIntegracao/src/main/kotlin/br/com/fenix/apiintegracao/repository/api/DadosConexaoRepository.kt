package br.com.fenix.apiintegracao.repository.api

import br.com.fenix.apiintegracao.enums.Mapeamento
import br.com.fenix.apiintegracao.enums.Tipo
import br.com.fenix.apiintegracao.model.api.DadosConexao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DadosConexaoRepository : JpaRepository<DadosConexao, String> {

    @Query(value = "SELECT t FROM DadosConexao t WHERE t.tipo = ?1")
    fun findByBase(base: Tipo): Optional<DadosConexao>

    fun findByAtivoIsTrueAndMapeamentoEquals(mapeamento: Mapeamento): List<DadosConexao>
}