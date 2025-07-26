package br.com.fenix.apiintegracao.controller.decksubtitle

import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.repository.DynamicRepositoryRegistry
import br.com.fenix.apiintegracao.repository.decksubtitle.LegendaRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(DECK_SUBTITLE)
@Tag(name = "Legendas", description = "Endpoint para tabela de legendas")
class LegendaController(private val registry: DynamicRepositoryRegistry, assembler: PagedResourcesAssembler<LegendaDto>) : ControllerJpaBase<UUID?, Legenda, LegendaDto, LegendaController, LegendaRepository>(assembler) {
    override fun getDynamicRegistry(): DynamicRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.DECKSUBTITLE
}