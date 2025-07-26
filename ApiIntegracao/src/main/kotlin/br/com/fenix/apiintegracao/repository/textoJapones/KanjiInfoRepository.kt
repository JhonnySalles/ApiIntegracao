package br.com.fenix.apiintegracao.repository.textojapones

import br.com.fenix.apiintegracao.model.textojapones.KanjiInfo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface KanjiInfoRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<KanjiInfo, UUID?> {

}