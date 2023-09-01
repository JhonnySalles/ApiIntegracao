package br.com.fenix.apiIntegracao.controller.textojapones

import br.com.fenix.apiIntegracao.controller.ControllerJpaBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_JAPONES_FILA_SQL
import br.com.fenix.apiIntegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiIntegracao.model.textojapones.FilaSql
import br.com.fenix.apiIntegracao.repository.textojapones.FilaSqlRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_JAPONES_FILA_SQL)
@Tag(name = "Fila SQL", description = "Endpoint para tabela de filas de SQL")
class FilaSqlController(repository: FilaSqlRepository, assembler: PagedResourcesAssembler<VocabularioDto>) : ControllerJpaBase<UUID?, FilaSql, VocabularioDto, FilaSqlController>(repository, assembler) {

}