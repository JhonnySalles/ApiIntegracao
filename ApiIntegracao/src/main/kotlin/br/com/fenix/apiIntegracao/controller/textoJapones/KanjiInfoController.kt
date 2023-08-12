package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_KANJI_INFO
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textojapones.KanjiInfoDto
import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import br.com.fenix.apiIntegracao.repository.textojapones.KanjiInfoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_KANJI_INFO)
@Tag(name = "Kanji Info", description = "Endpoint para tabela de informações do kanji")
class KanjiInfoController(repository: KanjiInfoRepository) : Controller<UUID?, KanjiInfo, KanjiInfoDto>(repository) {

}