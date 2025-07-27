package br.com.fenix.apiintegracao.repository.processatexto

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.RepositoryDaoBase
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.processatexto.ComicInfo
import br.com.fenix.apiintegracao.repository.RepositoryJdbcBase
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ComicInfoRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcBase<ComicInfo, UUID?>() {

    override val dao : RepositoryDaoBase<UUID?, ComicInfo> by lazy { DaoFactory.createComicInfoDao(registry.getConnection(Conexao.PROCESSA_TEXTO)) as RepositoryDaoBase<UUID?, ComicInfo> }

}