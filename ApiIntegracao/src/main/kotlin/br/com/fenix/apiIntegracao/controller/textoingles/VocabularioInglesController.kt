package br.com.fenix.apiintegracao.controller.textoingles

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_INGLES_VOCABULARIO
import br.com.fenix.apiintegracao.dto.textoingles.VocabularioDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textoingles.VocabularioIngles
import br.com.fenix.apiintegracao.repository.textoingles.VocabularioInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_VOCABULARIO)
@Tag(name = "Vocabulário Inglês", description = "Endpoint de vocabulários inglês")
class VocabularioInglesController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, VocabularioIngles, VocabularioDto, VocabularioInglesController, VocabularioInglesRepository>(VocabularioIngles.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_INGLES

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper
}