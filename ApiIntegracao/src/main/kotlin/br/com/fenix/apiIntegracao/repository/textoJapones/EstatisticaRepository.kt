package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import org.springframework.data.jpa.repository.JpaRepository

interface EstatisticaRepository : JpaRepository<Estatistica, String> {

}