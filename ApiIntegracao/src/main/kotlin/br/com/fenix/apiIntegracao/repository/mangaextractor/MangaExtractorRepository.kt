package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.ExtractorDaoBase
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.repository.ExtractorRepositoryBase
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class MangaExtractorRepository(private val registry : DynamicJdbcRegistry): ExtractorRepositoryBase<MangaVolume, UUID?>(MangaVolume.Companion) {

    override val dao : ExtractorDaoBase<MangaVolume, UUID?> by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun getTabela(obj: MangaVolume): String = obj.manga

}