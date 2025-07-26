package br.com.fenix.apiintegracao.repository.textojapones

import br.com.fenix.apiintegracao.model.textojapones.FilaSqlJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FilaSqlJaponesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<FilaSqlJapones, UUID?> {

}