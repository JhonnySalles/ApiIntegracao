package br.com.fenix.apiintegracao.controller.withjson

import br.com.fenix.apiintegracao.TestConfigs
import br.com.fenix.apiintegracao.controller.BaseControllerTest
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.TEXTO_JAPONES
import br.com.fenix.apiintegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiintegracao.dto.wrapper.WrapperEstatisticaDto
import br.com.fenix.apiintegracao.mock.MockEstatistica
import br.com.fenix.apiintegracao.model.textojapones.EstatisticaJapones
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class VocabularioJaponesControllerJsonTest() : BaseControllerTest<UUID?, EstatisticaJapones, EstatisticaDto, WrapperEstatisticaDto>(MockEstatistica(), TestConfigs.CONTENT_TYPE_JSON) {

    override var testName: String = "Estatistica Controller Json Test"
    override var pathEndpointObject: String = TEXTO_JAPONES
    override var pathEndpointList: String = "$TEXTO_JAPONES/lista"
    override var pathEndpointPaginadoAtualizacao: String = "$TEXTO_JAPONES/atualizacao"
    override var pathEndpointListaAtualizacao: String = "$TEXTO_JAPONES/lista/atualizacao"

    override fun asserts(older: EstatisticaDto, new: EstatisticaDto) {
        Assertions.assertNotNull(new)
        Assertions.assertNotNull(new.getId())
        Assertions.assertNotNull(new.kanji)
        Assertions.assertNotNull(new.leitura)
        Assertions.assertTrue(new.getId() != null)
        Assertions.assertEquals(older.kanji, new.kanji)
        Assertions.assertEquals(older.leitura, new.leitura)
    }

    override fun updateObject(objeto: EstatisticaDto): EstatisticaDto {
        objeto.leitura = "troca de leitura"
        return objeto
    }

}