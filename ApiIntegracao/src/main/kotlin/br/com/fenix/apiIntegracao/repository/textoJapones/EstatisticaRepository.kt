package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.Estatistica
import org.springframework.data.jpa.repository.JpaRepository

interface EstatisticaRepository : JpaRepository<Estatistica, String> {

}