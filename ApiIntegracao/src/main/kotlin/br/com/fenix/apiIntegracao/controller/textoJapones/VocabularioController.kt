package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.model.textojapones.Vocabulario
import br.com.fenix.apiIntegracao.repository.textojapones.VocabularioRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
@Tag(name = "Vocabulário", description = "Endpoint para tabela de vocabulários")
class VocabularioController(repository: VocabularioRepository, assembler: PagedResourcesAssembler<VocabularioDto>) : ControllerJpaBase<UUID?, Vocabulario, VocabularioDto, VocabularioController>(repository, assembler) {

}