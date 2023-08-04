package br.com.fenix.apiIntegracao.controller.textoJapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.model.textoJapones.Estatistica
import br.com.fenix.apiIntegracao.repository.Repository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
class EstatisticaController(repository: Repository<Estatistica, Long>) : Controller<Estatistica, Long>(repository) {

}