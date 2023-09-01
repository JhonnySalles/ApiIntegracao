package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_REVISAR
import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.model.textojapones.Revisar
import br.com.fenix.apiIntegracao.repository.textojapones.RevisarRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_REVISAR)
@Tag(name = "Revisar", description = "Endpoint para tabela de revisão")
class RevisarController(repository: RevisarRepository, assembler: PagedResourcesAssembler<RevisarDto>) : ControllerJpaBase<UUID?, Revisar, RevisarDto, RevisarController>(repository, assembler) {

}