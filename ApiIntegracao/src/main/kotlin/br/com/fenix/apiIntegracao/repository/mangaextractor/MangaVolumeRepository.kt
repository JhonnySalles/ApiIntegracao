package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.ExtractorDaoBase
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.repository.ExtractorRepositoryBase
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MangaVolumeRepository(private val registry : DynamicJdbcRegistry): ExtractorRepositoryBase<MangaVolume, UUID?>(MangaVolume.Companion) {

    override val dao : ExtractorDaoBase<MangaVolume, UUID?> by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun getTabela(obj: MangaVolume): String = obj.manga

}