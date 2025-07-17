package br.com.fenix.apiIntegracao.controller.mangaextractor

import br.com.fenix.apiIntegracao.controller.ControllerJdbcBase
import br.com.fenix.apiIntegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR
import br.com.fenix.apiIntegracao.dto.mangaextractor.VolumeDto
import br.com.fenix.apiIntegracao.model.mangaextractor.Volume
import br.com.fenix.apiIntegracao.multitenant.TenantRoutingDatasource
import br.com.fenix.apiIntegracao.repository.mangaextractor.MangaExtractorRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR)
@Tag(name = "MangaExtractor", description = "Endpoint para base MangaExtractor")
class MangaExtractorController(assembler: PagedResourcesAssembler<VolumeDto>, routing : TenantRoutingDatasource) : ControllerJdbcBase<UUID?, Volume, VolumeDto, MangaExtractorController>(MangaExtractorRepository(routing), assembler) {

}