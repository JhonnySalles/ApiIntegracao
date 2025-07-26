package br.com.fenix.apiintegracao.repository.textoingles

import br.com.fenix.apiintegracao.model.textoingles.ValidoIngles
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ValidoInglesRepository : br.com.fenix.apiintegracao.repository.RepositoryJpaBase<ValidoIngles, UUID?> {

}