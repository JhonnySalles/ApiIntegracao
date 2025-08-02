package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES_ESTATISTICA
import br.com.fenix.apiintegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textojapones.EstatisticaJapones
import br.com.fenix.apiintegracao.repository.textojapones.EstatisticaJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_ESTATISTICA)
@Tag(name = "Estatística Japônes", description = "Endpoint de estatísticas japônes")
class EstatisticaJaponesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, EstatisticaJapones, EstatisticaDto, EstatisticaJaponesController, EstatisticaJaponesRepository>(EstatisticaJapones.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES
}