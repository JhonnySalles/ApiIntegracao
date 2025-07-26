package br.com.fenix.apiintegracao.repository.decksubtitle

import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.repository.RepositoryJpaBase
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LegendaRepository : RepositoryJpaBase<Legenda, UUID?> {

}