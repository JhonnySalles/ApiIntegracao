package br.com.fenix.apiIntegracao.controller.mangaextractor

import br.com.fenix.apiIntegracao.controller.ControllerJdbcBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.TEXTO_INGLES_VOCABULARIO
import br.com.fenix.apiIntegracao.dto.mangaextractor.VolumeDto
import br.com.fenix.apiIntegracao.model.mangaextractor.Volume
import br.com.fenix.apiIntegracao.repository.mangaextractor.MangaExtractorRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_VOCABULARIO)
@Tag(name = "MangaExtractor", description = "Endpoint para base MangaExtractor")
class MangaExtractorController(assembler: PagedResourcesAssembler<VolumeDto>) : ControllerJdbcBase<UUID?, Volume, VolumeDto, MangaExtractorController>(MangaExtractorRepository(), assembler) {

}