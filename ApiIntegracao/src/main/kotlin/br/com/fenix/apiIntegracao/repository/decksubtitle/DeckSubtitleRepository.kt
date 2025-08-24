package br.com.fenix.apiintegracao.repository.decksubtitle

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiintegracao.enums.Conexao
import br.com.fenix.apiintegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.repository.RepositoryJdbcTabela
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class DeckSubtitleRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcTabela<Legenda, UUID?> {

    private val dao : DeckSubtitleDao  by lazy { DaoFactory.createDeckSubtitleDao(registry.getSource(Conexao.DECKSUBTITLE)) }

    override fun createtabela(tabela: String) {
        dao.createTable(tabela)
    }

    override fun existstabela(tabela: String): Boolean {
        return dao.existTable(tabela)
    }

    override fun tabelas(): List<String> {
        return dao.tables
    }
    
    override fun update(tabela: String, obj: Legenda): Legenda {
        dao.update(tabela, obj)
        return obj
    }

    override fun insert(tabela: String, obj: Legenda): Legenda {
        obj.setId(dao.insert(tabela, obj))
        return obj
    }

    override fun select(tabela: String, id: UUID?): Optional<Legenda> {
        if (id == null)
            return Optional.empty()

        return Optional.ofNullable(dao.select(tabela, id))
    }

    override fun update(obj: Legenda): Legenda {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun insert(obj: Legenda): Legenda {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun select(id: UUID?): Optional<Legenda> {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun findAll(tabela: String): List<Legenda> {
        return dao.selectAll(tabela)
    }

    override fun findAll(tabela: String, pageable: Pageable?): Page<Legenda> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAll(tabela, pageable)
    }

    override fun findAll(): List<Legenda> {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun findAll(pageable: Pageable?): Page<Legenda> {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<Legenda> {
        return dao.selectAll(tabela, dateTime)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAll(tabela, dateTime, pageable)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime): List<Legenda> {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun delete(id: UUID?) {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun delete(obj: Legenda) {
        TODO("Não implementado esta consulta, use o endpoint por tabela")
    }

    override fun delete(tabela: String, id: UUID?) {
        select(tabela, id).ifPresent { dao.delete(tabela, it) }
    }

    override fun delete(tabela: String, obj: Legenda) {
        dao.delete(tabela, obj)
    }
}