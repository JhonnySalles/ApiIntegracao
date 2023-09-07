package br.com.fenix.apiIntegracao.controller.decksubtitle

import br.com.fenix.apiIntegracao.controller.ControllerJdbcBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_INGLES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.repository.decksubtitle.DeckSubtitleRepository
import br.com.fenix.apiIntegracao.service.api.TabelasService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_VOCABULARIO)
@Tag(name = "DeckSubtitle", description = "Endpoint para base DeckSubtitle")
class DeckSubtitleController(@Autowired tabelas : TabelasService, assembler: PagedResourcesAssembler<LegendaDto>) : ControllerJdbcBase<UUID?, Legenda, LegendaDto, DeckSubtitleController>(DeckSubtitleRepository(tabelas), assembler) {

}