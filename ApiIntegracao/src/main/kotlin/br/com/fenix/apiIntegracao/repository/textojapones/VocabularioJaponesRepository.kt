package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.VocabularioJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioJaponesRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<VocabularioJapones, UUID?> {

}