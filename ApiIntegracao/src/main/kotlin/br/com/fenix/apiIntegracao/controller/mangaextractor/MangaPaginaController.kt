package br.com.fenix.apiintegracao.controller.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemSmall
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR_PAGINA
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaPaginaDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaPagina
import br.com.fenix.apiintegracao.repository.mangaextractor.MangaPaginaRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR_PAGINA)
@Tag(name = "Manga Volume — Páginas", description = "Endpoint para consultas de páginas de manga")
class MangaPaginaController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseItemSmall<UUID?, MangaPagina, MangaPaginaDto, MangaPaginaController>(MangaPaginaRepository(registry), MangaPagina.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}