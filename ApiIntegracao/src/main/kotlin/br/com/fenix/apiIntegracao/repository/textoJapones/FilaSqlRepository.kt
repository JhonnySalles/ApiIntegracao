package br.com.fenix.apiIntegracao.repository.textoJapones

import br.com.fenix.apiIntegracao.model.textoJapones.FilaSql
import org.springframework.data.jpa.repository.JpaRepository

interface FilaSqlRepository : JpaRepository<FilaSql, String> {

}