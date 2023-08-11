package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textojapones.KanjaxPtDto
import br.com.fenix.apiIntegracao.model.textojapones.KanjaxPt
import br.com.fenix.apiIntegracao.repository.textojapones.KanjaxPtRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
@Tag(name = "Kanjax", description = "Endpoint para tabela do Kanjax")
class KanjaxPtController(repository: KanjaxPtRepository) : Controller<UUID?, KanjaxPt, KanjaxPtDto>(repository) {

}