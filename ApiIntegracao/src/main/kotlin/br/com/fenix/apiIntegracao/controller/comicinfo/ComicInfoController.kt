package br.com.fenix.apiintegracao.controller.comicinfo

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.COMIC_INFO
import br.com.fenix.apiintegracao.dto.processatexto.ComicInfoDto
import br.com.fenix.apiintegracao.model.processatexto.ComicInfo
import br.com.fenix.apiintegracao.repository.processatexto.ComicInfoRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(COMIC_INFO)
@Tag(name = "Comic Info", description = "Endpoint de comic info")
class ComicInfoController(registry: DynamicJdbcRegistry) : ControllerJdbcBase<UUID?, ComicInfo, ComicInfoDto, ComicInfoController>(ComicInfoRepository(registry), ComicInfo.Companion) {

    @Autowired
    private lateinit var modelMapper: ModelMapper
    override fun getMapper(): ModelMapper = modelMapper

}