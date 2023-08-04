package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.KanjaxPt
import org.springframework.data.jpa.repository.JpaRepository

interface KanjaxPtRepository : JpaRepository<KanjaxPt, String> {

}