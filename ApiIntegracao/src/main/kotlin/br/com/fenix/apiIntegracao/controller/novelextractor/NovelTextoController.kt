package br.com.fenix.apiintegracao.controller.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseParent
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.NOVEL_EXTRACTOR_TEXTO
import br.com.fenix.apiintegracao.dto.novelextractor.NovelTextoDto
import br.com.fenix.apiintegracao.model.novelextractor.NovelTexto
import br.com.fenix.apiintegracao.repository.novelextractor.NovelTextoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOVEL_EXTRACTOR_TEXTO)
@Tag(name = "Novel Volume — Textos", description = "Endpoint para consultas de texto de novel")
class NovelTextoController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseParent<UUID?, NovelTexto, NovelTextoDto, NovelTextoController>(NovelTextoRepository(registry), NovelTexto.Companion) {

}