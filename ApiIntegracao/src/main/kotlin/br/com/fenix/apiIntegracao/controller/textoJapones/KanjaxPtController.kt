package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_KANJAX_PT
import br.com.fenix.apiIntegracao.dto.textojapones.KanjaxPtDto
import br.com.fenix.apiIntegracao.model.textojapones.KanjaxPt
import br.com.fenix.apiIntegracao.repository.textojapones.KanjaxPtRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_KANJAX_PT)
@Tag(name = "Kanjax", description = "Endpoint para tabela do Kanjax")
class KanjaxPtController(repository: KanjaxPtRepository, assembler: PagedResourcesAssembler<KanjaxPtDto>) : ControllerJpaBase<UUID?, KanjaxPt, KanjaxPtDto, KanjaxPtController>(repository, assembler) {

}