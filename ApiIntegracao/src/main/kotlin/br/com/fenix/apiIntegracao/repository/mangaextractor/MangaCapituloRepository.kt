package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParametersIsNullException
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapitulo
import br.com.fenix.apiintegracao.repository.RepositoryJdbcParent
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MangaCapituloRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcParent<MangaCapitulo, UUID?> {

    private val dao : MangaExtractorDao by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<MangaCapitulo> = dao.selectCapitulo(tabela, id)

    override fun update(tabela: String, obj: MangaCapitulo): MangaCapitulo = dao.updateCapitulo(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: MangaCapitulo): MangaCapitulo {
        obj.setId(dao.insertCapitulo(tabela, idParent ?: throw RequiredParametersIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<MangaCapitulo> = dao.selectAllCapitulos(tabela, idParent ?: throw RequiredParametersIsNullException())

    override fun delete(tabela: String, obj: MangaCapitulo) = dao.deleteCapitulo(tabela, obj)

    override fun delete(tabela: String, id: UUID?) = dao.deleteCapitulo(tabela, MangaCapitulo(id))

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}