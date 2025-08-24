package br.com.fenix.apiintegracao.repository.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.NovelExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParametersIsNullException
import br.com.fenix.apiintegracao.model.novelextractor.NovelCapitulo
import br.com.fenix.apiintegracao.repository.RepositoryJdbcParent
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NovelCapituloRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcParent<NovelCapitulo, UUID?> {

    private val dao : NovelExtractorDao by lazy { DaoFactory.createNovelExtractorDao(registry.getSource(Conexao.NOVEL_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<NovelCapitulo> = dao.selectCapitulo(tabela, id)

    override fun update(tabela: String, obj: NovelCapitulo): NovelCapitulo = dao.updateCapitulo(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: NovelCapitulo): NovelCapitulo {
        obj.setId(dao.insertCapitulo(tabela, idParent ?: throw RequiredParametersIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<NovelCapitulo> = dao.selectAllCapitulos(tabela, idParent ?: throw RequiredParametersIsNullException())

    override fun delete(tabela: String, obj: NovelCapitulo) = dao.deleteCapitulo(tabela, obj)

    override fun delete(tabela: String, id: UUID?) = dao.deleteCapitulo(tabela, NovelCapitulo(id))

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}