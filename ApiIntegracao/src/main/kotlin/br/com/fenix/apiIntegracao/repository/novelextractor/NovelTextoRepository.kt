package br.com.fenix.apiintegracao.repository.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.NovelExtractorDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredParametersIsNullException
import br.com.fenix.apiintegracao.model.novelextractor.NovelTexto
import br.com.fenix.apiintegracao.repository.RepositoryJdbcParent
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NovelTextoRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcParent<NovelTexto, UUID?> {

    private val dao : NovelExtractorDao by lazy { DaoFactory.createNovelExtractorDao(registry.getSource(Conexao.NOVEL_EXTRACTOR)) }

    override fun select(tabela: String, id: UUID?): Optional<NovelTexto> = dao.selectTexto(tabela, id)

    override fun update(tabela: String, obj: NovelTexto): NovelTexto = dao.updateTexto(tabela, obj)

    override fun insert(tabela: String, idParent: UUID?, obj: NovelTexto): NovelTexto {
        obj.setId(dao.insertTexto(tabela, idParent ?: throw RequiredParametersIsNullException(), obj))
        return obj
    }

    override fun findAll(tabela: String, idParent: UUID?): List<NovelTexto> = dao.selectAllTextos(tabela, idParent ?: throw RequiredParametersIsNullException())

    override fun delete(tabela: String, obj: NovelTexto) = dao.deleteTexto(tabela, obj)

    override fun delete(tabela: String, id: UUID?) = dao.deleteTexto(tabela, NovelTexto(id))

    override fun existstabela(tabela: String): Boolean = dao.existTable(tabela)

}