package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface KanjiInfoRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<KanjiInfo, UUID?> {

}