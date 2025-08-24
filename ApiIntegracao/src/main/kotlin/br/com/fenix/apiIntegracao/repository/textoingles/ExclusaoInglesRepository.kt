package br.com.fenix.apiintegracao.repository.textoingles

import br.com.fenix.apiintegracao.model.textoingles.ExclusaoIngles
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExclusaoInglesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<ExclusaoIngles, UUID?> {

}