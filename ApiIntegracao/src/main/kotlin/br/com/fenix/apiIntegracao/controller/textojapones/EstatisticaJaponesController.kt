package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_ESTATISTICA
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.model.textojapones.EstatisticaJapones
import br.com.fenix.apiIntegracao.repository.textojapones.EstatisticaJaponesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_ESTATISTICA)
@Tag(name = "Estatistica", description = "Endpoint para tabela de estatisticas dos kanjis")
class EstatisticaJaponesController(repository: EstatisticaJaponesRepository, assembler: PagedResourcesAssembler<EstatisticaDto>) : ControllerJpaBase<UUID?, EstatisticaJapones, EstatisticaDto, EstatisticaJaponesController>(repository, assembler) {

}