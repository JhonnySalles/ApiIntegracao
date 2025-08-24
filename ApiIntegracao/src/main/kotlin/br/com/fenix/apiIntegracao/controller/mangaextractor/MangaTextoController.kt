package br.com.fenix.apiintegracao.controller.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseParent
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR_TEXTO
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaTextoDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaTexto
import br.com.fenix.apiintegracao.repository.mangaextractor.MangaTextoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR_TEXTO)
@Tag(name = "Manga Volume — Textos", description = "Endpoint para consultas de textos de manga")
class MangaTextoController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseParent<UUID?, MangaTexto, MangaTextoDto, MangaTextoController>(MangaTextoRepository(registry), MangaTexto.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}