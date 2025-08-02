package br.com.fenix.apiintegracao.repository.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.NovelExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.EndpointUnavailableException
import br.com.fenix.apiintegracao.exceptions.RequiredParameterstIsNullException
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.model.novelextractor.NovelVolume
import br.com.fenix.apiintegracao.repository.ExtractorRepositoryBase
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class NovelExtractorRepository(private val registry : DynamicJdbcRegistry): ExtractorRepositoryBase<NovelVolume, UUID?>(NovelVolume.Companion) {

    override val dao : NovelExtractorDao by lazy { DaoFactory.createNovelExtractorDao(registry.getSource(Conexao.NOVEL_EXTRACTOR)) }

    override fun getTabela(obj: NovelVolume): String = obj.novel

}