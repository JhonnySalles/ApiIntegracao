package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.model.textojapones.VocabularioJapones
import br.com.fenix.apiintegracao.repository.DynamicRepositoryRegistry
import br.com.fenix.apiintegracao.repository.textojapones.VocabularioJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES)
@Tag(name = "Vocabulário Japônes", description = "Endpoint de vocabulários japônes")
class VocabularioController(private val registry: DynamicRepositoryRegistry, assembler: PagedResourcesAssembler<VocabularioDto>) : ControllerJpaBase<UUID?, VocabularioJapones, VocabularioDto, VocabularioController, VocabularioJaponesRepository>(assembler) {
    override fun getDynamicRegistry(): DynamicRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES
}