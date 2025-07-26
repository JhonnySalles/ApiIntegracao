package br.com.fenix.apiIntegracao.controller.withjson

import br.com.fenix.apiIntegracao.TestConfigs
import br.com.fenix.apiIntegracao.controller.BaseControllerTest
import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import br.com.fenix.apiIntegracao.dto.wrapper.WrapperEstatisticaDto
import br.com.fenix.apiIntegracao.mock.MockEstatistica
import br.com.fenix.apiIntegracao.model.textojapones.EstatisticaJapones
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EstatisticaControllerJsonTest(
    override var testName: String = "Estatistica Controller Json Test",
    override var pathEndpointObject: String = "/api/texto-japones/estatistica",
    override var pathEndpointList: String = "/api/texto-japones/estatistica/lista",
    override var pathEndpointPaginadoAtualizacao: String = "/api/texto-japones/estatistica/atualizacao",
    override var pathEndpointListaAtualizacao: String = "/api/texto-japones/estatistica/lista/atualizacao"
) : BaseControllerTest<UUID?, EstatisticaJapones, EstatisticaDto, WrapperEstatisticaDto>(MockEstatistica(), TestConfigs.CONTENT_TYPE_JSON) {

    override fun asserts(older: EstatisticaDto, new: EstatisticaDto) {
        Assert.assertNotNull(new)
        Assert.assertNotNull(new.getId())
        Assert.assertNotNull(new.kanji)
        Assert.assertNotNull(new.leitura)
        Assertions.assertTrue(new.getId() != null)
        Assertions.assertEquals(older.kanji, new.kanji)
        Assertions.assertEquals(older.leitura, new.leitura)
    }

    override fun updateObject(objeto: EstatisticaDto): EstatisticaDto {
        objeto.leitura = "troca de leitura"
        return objeto
    }

}