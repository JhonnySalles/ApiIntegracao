package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.FilaSql
import org.springframework.data.jpa.repository.JpaRepository

interface FilaSqlRepository : JpaRepository<FilaSql, String> {

}