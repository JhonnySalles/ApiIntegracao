package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.Controller
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.model.textojapones.FilaSql
import br.com.fenix.apiIntegracao.repository.textojapones.FilaSqlRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_VOCABULARIO)
@Tag(name = "Fila SQL", description = "Endpoint para tabela de filas de SQL")
class FilaSqlController(repository: FilaSqlRepository) : Controller<UUID?, FilaSql, VocabularioDto>(repository, FilaSql::class.java, VocabularioDto::class.java) {

}