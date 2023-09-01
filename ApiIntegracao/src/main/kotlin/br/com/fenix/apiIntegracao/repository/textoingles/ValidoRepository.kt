package br.com.fenix.apiIntegracao.repository.textoingles

import br.com.fenix.apiIntegracao.model.textoingles.Valido
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ValidoRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<Valido, UUID?> {

}