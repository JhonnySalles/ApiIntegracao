package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES_EXCLUSAO
import br.com.fenix.apiintegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textojapones.ExclusaoJapones
import br.com.fenix.apiintegracao.repository.textojapones.ExclusaoJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_EXCLUSAO)
@Tag(name = "Vocabulário Japônes — Exclusões", description = "Endpoint de vocabulários excluidos japônes")
class ExclusaoJaponesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, ExclusaoJapones, ExclusaoDto, ExclusaoJaponesController, ExclusaoJaponesRepository>(ExclusaoJapones.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper
}