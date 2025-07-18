package br.com.fenix.apiIntegracao.controller.textoingles

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_INGLES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textoingles.VocabularioDto
import br.com.fenix.apiIntegracao.model.textoingles.VocabularioIngles
import br.com.fenix.apiIntegracao.repository.textoingles.VocabularioInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_VOCABULARIO)
@Tag(name = "Vocabulário", description = "Endpoint para tabela de vocabulários")
class VocabularioInglesController(repository: VocabularioInglesRepository, assembler: PagedResourcesAssembler<VocabularioDto>) : ControllerJpaBase<UUID?, VocabularioIngles, VocabularioDto, VocabularioInglesController>(repository, assembler) {

}