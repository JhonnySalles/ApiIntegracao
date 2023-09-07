package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.ExclusaoJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExclusaoJaponesRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<ExclusaoJapones, UUID?> {

}