package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Exclusao
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExclusaoRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<Exclusao, UUID?> {

}