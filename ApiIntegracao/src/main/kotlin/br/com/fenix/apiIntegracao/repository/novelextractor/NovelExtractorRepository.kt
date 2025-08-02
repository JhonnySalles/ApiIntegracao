package br.com.fenix.apiintegracao.repository.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.NovelExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.model.novelextractor.NovelVolume
import br.com.fenix.apiintegracao.repository.ExtractorRepositoryBase
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NovelExtractorRepository(private val registry : DynamicJdbcRegistry): ExtractorRepositoryBase<NovelVolume, UUID?>(NovelVolume.Companion) {

    override val dao : NovelExtractorDao by lazy { DaoFactory.createNovelExtractorDao(registry.getSource(Conexao.NOVEL_EXTRACTOR)) }

    override fun getTabela(obj: NovelVolume): String = obj.novel

}