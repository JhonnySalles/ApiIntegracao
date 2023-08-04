package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.Vocabulario
import org.springframework.stereotype.Repository

@Repository
interface VocabularioRepository : br.com.fenix.apiIntegracao.repository.Repository<Vocabulario, String> {

}