package br.com.fenix.apiIntegracao.repository.textojapones

import br.com.fenix.apiIntegracao.model.textojapones.EstatisticaJapones
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EstatisticaJaponesRepository : br.com.fenix.apiIntegracao.repository.RepositoryJpaBase<EstatisticaJapones, UUID?> {

    @Query(value = "select e from EstatisticaJapones e ")
    fun selectTex()
}