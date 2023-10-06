package br.com.fenix.apiIntegracao.controller.decksubtitle

import br.com.fenix.apiIntegracao.controller.ControllerJdbcBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiIntegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.multitenant.TenantRoutingDatasource
import br.com.fenix.apiIntegracao.repository.decksubtitle.DeckSubtitleRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(DECK_SUBTITLE)
@Tag(name = "DeckSubtitle", description = "Endpoint para base DeckSubtitle")
class DeckSubtitleController(assembler: PagedResourcesAssembler<LegendaDto>, routing : TenantRoutingDatasource) : ControllerJdbcBase<UUID?, Legenda, LegendaDto, DeckSubtitleController>(DeckSubtitleRepository(routing), assembler) {

}