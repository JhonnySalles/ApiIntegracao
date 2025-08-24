package br.com.fenix.apiintegracao.controller.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaVolumeDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.repository.mangaextractor.MangaVolumeRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR)
@Tag(name = "Manga Volume", description = "Endpoint para base manga extractor")
class MangaVolumeController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseTabela<UUID?, MangaVolume, MangaVolumeDto, MangaVolumeController>(MangaVolumeRepository(registry), MangaVolume.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}