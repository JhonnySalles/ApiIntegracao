package br.com.fenix.apiintegracao.controller.decksubtitle

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.repository.decksubtitle.DeckSubtitleRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(DECK_SUBTITLE)
@Tag(name = "Legenda", description = "Endpoint de legendas")
class LegendaController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseTabela<UUID?, Legenda, LegendaDto, LegendaController>(DeckSubtitleRepository(registry), Legenda.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}