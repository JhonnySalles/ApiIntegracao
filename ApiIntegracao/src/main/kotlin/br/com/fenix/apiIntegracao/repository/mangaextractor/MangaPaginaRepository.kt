package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParametersIsNullException
import br.com.fenix.apiintegracao.model.mangaextractor.MangaPagina
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemFull
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemSmall
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MangaPaginaRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcItemSmall<MangaPagina, UUID?> {

    private val dao : MangaExtractorDao by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<MangaPagina> = dao.selectPagina(tabela, id)

    override fun update(tabela: String, obj: MangaPagina): MangaPagina = dao.updatePagina(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: MangaPagina): MangaPagina {
        obj.setId(dao.insertPagina(tabela, idParent ?: throw RequiredParametersIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<MangaPagina> = dao.selectAllPaginas(tabela, idParent ?: throw RequiredParametersIsNullException())

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}