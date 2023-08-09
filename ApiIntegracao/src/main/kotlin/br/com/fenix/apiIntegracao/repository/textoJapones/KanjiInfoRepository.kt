package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KanjiInfoRepository : br.com.fenix.apiIntegracao.repository.Repository<KanjiInfo, String?> {

}