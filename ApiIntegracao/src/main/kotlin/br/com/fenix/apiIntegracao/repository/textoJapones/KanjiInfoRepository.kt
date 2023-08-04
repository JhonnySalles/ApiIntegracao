package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.KanjiInfo
import org.springframework.data.jpa.repository.JpaRepository

interface KanjiInfoRepository : JpaRepository<KanjiInfo, String> {

}