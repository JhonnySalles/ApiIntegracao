package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.Exclusao
import org.springframework.data.jpa.repository.JpaRepository

interface ExclusaoRepository : JpaRepository<Exclusao, String> {

}