package br.com.fenix.apiintegracao.controller.textoingles

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_INGLES_EXCLUSAO
import br.com.fenix.apiintegracao.dto.textoingles.ExclusaoDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.novelextractor.NovelVolume
import br.com.fenix.apiintegracao.model.textojapones.ExclusaoJapones
import br.com.fenix.apiintegracao.repository.textojapones.ExclusaoJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_EXCLUSAO)
@Tag(name = "Exclusão Inglês", description = "Endpoint de vocabulários excluidos inglês")
class ExclusaoInglesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, ExclusaoJapones, ExclusaoDto, ExclusaoInglesController, ExclusaoJaponesRepository>(ExclusaoJapones.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_INGLES
}