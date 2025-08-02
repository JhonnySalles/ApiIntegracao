package br.com.fenix.apiintegracao.controller.textoingles

import br.com.fenix.apiintegracao.component.DynamicJpaRepositoryRegistry
import br.com.fenix.apiintegracao.controller.ControllerJpaBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_INGLES_VALIDO
import br.com.fenix.apiintegracao.dto.textoingles.ValidoDto
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.textoingles.ValidoIngles
import br.com.fenix.apiintegracao.repository.textoingles.ValidoInglesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(TEXTO_INGLES_VALIDO)
@Tag(name = "Inglês Válido", description = "Endpoint de vocabulários validos em inglês")
class ValidoController(private val registry: DynamicJpaRepositoryRegistry) : ControllerJpaBase<UUID?, ValidoIngles, ValidoDto, ValidoController, ValidoInglesRepository>(ValidoIngles.Companion) {
    override fun getDynamicRegistry(): DynamicJpaRepositoryRegistry = registry
    override val conexao: Conexao = Conexao.TEXTO_INGLES
}