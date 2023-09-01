package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Vocabulario
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<Vocabulario, UUID?> {

}