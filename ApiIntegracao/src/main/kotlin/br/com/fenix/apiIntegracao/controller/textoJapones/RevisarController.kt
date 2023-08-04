package br.com.fenix.apiIntegracao.controller.textoJapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.model.textoJapones.Revisar
import br.com.fenix.apiIntegracao.repository.Repository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
class RevisarController(repository: Repository<Revisar, String>) : Controller<Revisar, String>(repository) {

}