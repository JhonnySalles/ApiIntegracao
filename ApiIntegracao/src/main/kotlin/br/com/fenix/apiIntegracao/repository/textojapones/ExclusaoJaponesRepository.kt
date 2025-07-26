package br.com.fenix.apiintegracao.repository.textojapones

import br.com.fenix.apiintegracao.model.textojapones.ExclusaoJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExclusaoJaponesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<ExclusaoJapones, UUID?> {

}