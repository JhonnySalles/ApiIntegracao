package br.com.fenix.apiIntegracao.repository.textoingles

import br.com.fenix.apiIntegracao.model.textoingles.Vocabulario
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<Vocabulario, UUID?> {

}