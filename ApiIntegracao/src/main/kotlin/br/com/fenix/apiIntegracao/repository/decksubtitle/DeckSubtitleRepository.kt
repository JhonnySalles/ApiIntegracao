package br.com.fenix.apiIntegracao.repository.decksubtitle

import br.com.fenix.apiIntegracao.database.dao.DaoFactory
import br.com.fenix.apiIntegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiIntegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiIntegracao.enums.Tipo
import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import br.com.fenix.apiIntegracao.service.api.TabelasService
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

class DeckSubtitleRepository: RepositoryJdbcBase<Legenda, UUID?> {

    @Bean
    private lateinit var tabelas : TabelasService

    private val dao : DeckSubtitleDao = DaoFactory.createDeckSubtitleDao(tabelas.getProperty(Tipo.DECKSUBTITLE))
    
    override fun update(tabela: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun insert(tabela: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun select(tabela: String, id: UUID?): Optional<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(tabela: String): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(tabela: String, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun createtabela(tabela: String) {
        TODO("Not yet implemented")
    }

    override fun existstabela(tabela: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun tabelas(): List<String> {
        TODO("Not yet implemented")
    }

    override fun delete(tabela: String, id: UUID?) {
        TODO("Not yet implemented")
    }

    override fun delete(tabela: String, obj: Legenda) {
        TODO("Not yet implemented")
    }
}