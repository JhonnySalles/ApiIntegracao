package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Exclusao
import org.springframework.data.jpa.repository.JpaRepository

interface ExclusaoRepository : JpaRepository<Exclusao, String> {

}