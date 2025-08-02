package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParameterstIsNullException
import br.com.fenix.apiintegracao.model.mangaextractor.MangaTexto
import br.com.fenix.apiintegracao.repository.RepositoryJdbcParent
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MangaTextoRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcParent<MangaTexto, UUID?> {

    private val dao : MangaExtractorDao by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<MangaTexto> = dao.selectTexto(tabela, id)

    override fun update(tabela: String, obj: MangaTexto): MangaTexto = dao.updateTexto(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: MangaTexto): MangaTexto {
        obj.setId(dao.insertTexto(tabela, idParent ?: throw RequiredParameterstIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<MangaTexto> = dao.selectAllTextos(tabela, idParent ?: throw RequiredParameterstIsNullException())

    override fun delete(tabela: String, obj: MangaTexto) = dao.deleteTexto(tabela, obj)

    override fun delete(tabela: String, id: UUID?) = dao.deleteTexto(tabela, MangaTexto(id))

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}