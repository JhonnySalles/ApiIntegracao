package br.com.fenix.apiintegracao.controller.textoingles

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_INGLES_REVISAR
import br.com.fenix.apiintegracao.dto.textoingles.RevisarDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textoingles.RevisarIngles
import br.com.fenix.apiintegracao.repository.textoingles.RevisarInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_REVISAR)
@Tag(name = "Vocabulário Inglês — Revisões", description = "Endpoint de vocabulários para revisão inglês")
class RevisarInglesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, RevisarIngles, RevisarDto, RevisarInglesController, RevisarInglesRepository>(RevisarIngles.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_INGLES
}