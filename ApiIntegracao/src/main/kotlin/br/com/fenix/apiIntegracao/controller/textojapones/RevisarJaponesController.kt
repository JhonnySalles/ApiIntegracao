package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES_REVISAR
import br.com.fenix.apiintegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textojapones.RevisarJapones
import br.com.fenix.apiintegracao.repository.textojapones.RevisarJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_REVISAR)
@Tag(name = "Vocabulário Japônes — Revisões", description = "Endpoint de vocabulários para revisão japônes")
class RevisarJaponesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, RevisarJapones, RevisarDto, RevisarJaponesController, RevisarJaponesRepository>(RevisarJapones.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES
}