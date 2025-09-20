package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParametersIsNullException
import br.com.fenix.apiintegracao.model.mangaextractor.MangaTexto
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemFull
import br.com.fenix.apiintegracao.repository.RepositoryJdbcItemSmall
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MangaTextoRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcItemSmall<MangaTexto, UUID?> {

    private val dao : MangaExtractorDao by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<MangaTexto> = dao.selectTexto(tabela, id)

    override fun update(tabela: String, obj: MangaTexto): MangaTexto = dao.updateTexto(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: MangaTexto): MangaTexto {
        obj.setId(dao.insertTexto(tabela, idParent ?: throw RequiredParametersIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<MangaTexto> = dao.selectAllTextos(tabela, idParent ?: throw RequiredParametersIsNullException())

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}