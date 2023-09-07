package br.com.fenix.apiIntegracao.repository.textoingles

import br.com.fenix.apiIntegracao.model.textoingles.VocabularioIngles
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularioInglesRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<VocabularioIngles, UUID?> {

}