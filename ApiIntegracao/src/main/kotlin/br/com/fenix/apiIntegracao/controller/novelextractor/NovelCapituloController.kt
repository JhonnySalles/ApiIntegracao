package br.com.fenix.apiintegracao.controller.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseParent
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.NOVEL_EXTRACTOR_CAPITULO
import br.com.fenix.apiintegracao.dto.novelextractor.NovelCapituloDto
import br.com.fenix.apiintegracao.model.novelextractor.NovelCapitulo
import br.com.fenix.apiintegracao.repository.novelextractor.NovelCapituloRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOVEL_EXTRACTOR_CAPITULO)
@Tag(name = "Novel Volume — Capítulos", description = "Endpoint para consultas de capítulos de novel")
class NovelCapituloController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseParent<UUID?, NovelCapitulo, NovelCapituloDto, NovelCapituloController>(NovelCapituloRepository(registry), NovelCapitulo.Companion) {

}