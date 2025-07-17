package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_REVISAR
import br.com.fenix.apiIntegracao.dto.textojapones.RevisarDto
import br.com.fenix.apiIntegracao.model.textojapones.RevisarJapones
import br.com.fenix.apiIntegracao.repository.textojapones.RevisarJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_REVISAR)
@Tag(name = "Revisar", description = "Endpoint para tabela de revisão")
class RevisarJaponesController(repository: RevisarJaponesRepository, assembler: PagedResourcesAssembler<RevisarDto>) : ControllerJpaBase<UUID?, RevisarJapones, RevisarDto, RevisarJaponesController>(repository, assembler) {

}