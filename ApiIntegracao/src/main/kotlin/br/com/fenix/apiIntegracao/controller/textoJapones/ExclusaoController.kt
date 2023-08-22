package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_EXCLUSAO
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiIntegracao.model.textojapones.Exclusao
import br.com.fenix.apiIntegracao.repository.textojapones.ExclusaoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_EXCLUSAO)
@Tag(name = "Exclusão", description = "Endpoint para tabela de exclusões de vocabulários")
class ExclusaoController(repository: ExclusaoRepository, assembler: PagedResourcesAssembler<ExclusaoDto>) : Controller<UUID?, Exclusao, ExclusaoDto, ExclusaoController>(repository, assembler) {

}