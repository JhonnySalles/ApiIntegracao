package br.com.fenix.apiIntegracao.controller.textoingles

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_INGLES_REVISAR
import br.com.fenix.apiIntegracao.dto.textoingles.RevisarDto
import br.com.fenix.apiIntegracao.model.textoingles.RevisarIngles
import br.com.fenix.apiIntegracao.repository.textoingles.RevisarInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_REVISAR)
@Tag(name = "Revisar", description = "Endpoint para tabela de revisão")
class RevisarInglesController(repository: RevisarInglesRepository, assembler: PagedResourcesAssembler<RevisarDto>) : ControllerJpaBase<UUID?, RevisarIngles, RevisarDto, RevisarInglesController>(repository, assembler) {

}