package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_ESTATISTICA
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import br.com.fenix.apiIntegracao.repository.textojapones.EstatisticaRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_ESTATISTICA)
@Tag(name = "Estatistica", description = "Endpoint para tabela de estatisticas dos kanjis")
class EstatisticaController(repository: EstatisticaRepository) : Controller<UUID?, Estatistica, EstatisticaDto>(repository) {

}