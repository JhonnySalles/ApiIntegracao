package br.com.fenix.apiintegracao.repository.textojapones

import br.com.fenix.apiintegracao.model.textojapones.VocabularioJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioJaponesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<VocabularioJapones, UUID?> {

}