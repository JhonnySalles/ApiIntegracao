package br.com.fenix.apiintegracao.controller.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.NOVEL_EXTRACTOR
import br.com.fenix.apiintegracao.dto.novelextractor.NovelVolumeDto
import br.com.fenix.apiintegracao.model.novelextractor.NovelVolume
import br.com.fenix.apiintegracao.repository.novelextractor.NovelVolumeRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOVEL_EXTRACTOR)
@Tag(name = "Novel Volume", description = "Endpoint para base novel extractor")
class NovelVolumeController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseTabela<UUID?, NovelVolume, NovelVolumeDto, NovelVolumeController>(NovelVolumeRepository(registry), NovelVolume.Companion) {

}