package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Vocabulario
import org.springframework.stereotype.Repository

@Repository
interface VocabularioRepository : br.com.fenix.apiIntegracao.repository.Repository<Vocabulario, String> {

}