package br.com.fenix.apiintegracao.controller.textoingles

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_INGLES_EXCLUSAO
import br.com.fenix.apiintegracao.dto.textoingles.ExclusaoDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textoingles.ExclusaoIngles
import br.com.fenix.apiintegracao.repository.textoingles.ExclusaoInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_EXCLUSAO)
@Tag(name = "Vocabulário Inglês — Exclusões", description = "Endpoint de vocabulários excluidos inglês")
class ExclusaoInglesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, ExclusaoIngles, ExclusaoDto, ExclusaoInglesController, ExclusaoInglesRepository>(ExclusaoIngles.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_INGLES
}