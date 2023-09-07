package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.FilaSqlJapones
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FilaSqlJaponesRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<FilaSqlJapones, UUID?> {

}