package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.KanjaxPt
import org.springframework.data.jpa.repository.JpaRepository

interface KanjaxPtRepository : JpaRepository<KanjaxPt, String> {

}