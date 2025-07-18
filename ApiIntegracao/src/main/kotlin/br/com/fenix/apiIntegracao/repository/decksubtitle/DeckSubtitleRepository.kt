package br.com.fenix.apiIntegracao.repository.decksubtitle

import br.com.fenix.apiIntegracao.database.dao.DaoFactory
import br.com.fenix.apiIntegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiIntegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiIntegracao.enums.Tipo
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import br.com.fenix.apiIntegracao.service.api.TabelasService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class DeckSubtitleRepository(var tabelas : TabelasService): RepositoryJdbcBase<Legenda, UUID?> {

    private val dao : DeckSubtitleDao = DaoFactory.createDeckSubtitleDao(tabelas.getProperty(Tipo.DECKSUBTITLE))

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

    override fun findAll(tabela: String): List<Legenda> {
        return dao.selectAll(tabela)
    }

    override fun findAll(tabela: String, pageable: Pageable?): Page<Legenda> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAll(tabela, pageable)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<Legenda> {
        return dao.selectAll(tabela, dateTime)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAll(tabela, dateTime, pageable)
    }

    override fun delete(tabela: String, id: UUID?) {
        select(tabela, id).ifPresent { dao.delete(tabela, it) }
    }

    override fun delete(tabela: String, obj: Legenda) {
        dao.delete(tabela, obj)
    }
}