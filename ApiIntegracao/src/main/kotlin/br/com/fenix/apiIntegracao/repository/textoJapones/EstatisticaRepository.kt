package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.Estatistica
import org.springframework.stereotype.Repository

@Repository
interface EstatisticaRepository : br.com.fenix.apiIntegracao.repository.Repository<Estatistica, String?> {

}