package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.KanjiInfo
import org.springframework.data.jpa.repository.JpaRepository

interface KanjiInfoRepository : JpaRepository<KanjiInfo, String> {

}