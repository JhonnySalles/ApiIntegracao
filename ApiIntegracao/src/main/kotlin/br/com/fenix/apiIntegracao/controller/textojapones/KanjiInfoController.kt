package br.com.fenix.apiintegracao.controller.textojapones

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES_KANJI_INFO
import br.com.fenix.apiintegracao.dto.textojapones.KanjiInfoDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textojapones.KanjiInfo
import br.com.fenix.apiintegracao.repository.textojapones.KanjiInfoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_KANJI_INFO)
@Tag(name = "Kanji", description = "Endpoint de informações de kanji")
class KanjiInfoController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, KanjiInfo, KanjiInfoDto, KanjiInfoController, KanjiInfoRepository>(KanjiInfo.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_JAPONES

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper
}