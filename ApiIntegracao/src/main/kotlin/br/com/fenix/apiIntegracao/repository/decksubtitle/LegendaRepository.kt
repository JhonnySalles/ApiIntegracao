package br.com.fenix.apiIntegracao.repository.decksubtitle

import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.repository.RepositoryJpaBase
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LegendaRepository : RepositoryJpaBase<Legenda, UUID?> {

}