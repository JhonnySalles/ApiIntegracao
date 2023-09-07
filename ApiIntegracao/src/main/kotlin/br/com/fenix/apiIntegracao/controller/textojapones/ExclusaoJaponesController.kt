package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_EXCLUSAO
import br.com.fenix.apiIntegracao.dto.textojapones.ExclusaoDto
import br.com.fenix.apiIntegracao.model.textojapones.ExclusaoJapones
import br.com.fenix.apiIntegracao.repository.textojapones.ExclusaoJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_EXCLUSAO)
@Tag(name = "Exclusão", description = "Endpoint para tabela de exclusões de vocabulários")
class ExclusaoJaponesController(repository: ExclusaoJaponesRepository, assembler: PagedResourcesAssembler<ExclusaoDto>) : ControllerJpaBase<UUID?, ExclusaoJapones, ExclusaoDto, ExclusaoJaponesController>(repository, assembler) {

}