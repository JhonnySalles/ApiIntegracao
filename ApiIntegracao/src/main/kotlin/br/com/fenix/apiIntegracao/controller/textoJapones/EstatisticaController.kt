package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import br.com.fenix.apiIntegracao.repository.Repository
import br.com.fenix.apiIntegracao.repository.textojapones.EstatisticaRepository
import br.com.fenix.apiIntegracao.repository.textojapones.ExclusaoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
@Tag(name = "Estatistica", description = "Endpoint para tabela de estatisticas dos kanjis")
class EstatisticaController(repository: EstatisticaRepository) : Controller<Estatistica, String?>(repository) {

}