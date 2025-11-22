package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES_KANJAX_PT
import br.com.fenix.apiintegracao.dto.textojapones.KanjaxPtDto
import br.com.fenix.apiintegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textojapones.KanjaxPt
import br.com.fenix.apiintegracao.model.textojapones.VocabularioJapones
import br.com.fenix.apiintegracao.repository.textojapones.KanjaxPtRepository
import br.com.fenix.apiintegracao.repository.textojapones.VocabularioJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_KANJAX_PT)
@Tag(name = "Kanjax", description = "Endpoint de vocabulários japônes")
class KanjaxController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, KanjaxPt, KanjaxPtDto, KanjaxController, KanjaxPtRepository>(KanjaxPt.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper
}