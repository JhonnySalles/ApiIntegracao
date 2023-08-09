package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.KanjaxPt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KanjaxPtRepository : br.com.fenix.apiIntegracao.repository.Repository<KanjaxPt, String?> {

}