package br.com.fenix.apiintegracao.controller.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemFull
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR_CAPITULO
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaCapituloDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapitulo
import br.com.fenix.apiintegracao.repository.mangaextractor.MangaCapituloRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR_CAPITULO)
@Tag(name = "Manga Volume — Capítulos", description = "Endpoint para consultas de capítulos de manga")
class MangaCapituloController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseItemFull<UUID?, MangaCapitulo, MangaCapituloDto, MangaCapituloController>(MangaCapituloRepository(registry), MangaCapitulo.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}