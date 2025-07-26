package br.com.fenix.apiintegracao.repository.textoingles

import br.com.fenix.apiintegracao.model.textoingles.VocabularioIngles
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioInglesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<VocabularioIngles, UUID?> {

}