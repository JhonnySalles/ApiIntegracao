package br.com.fenix.apiIntegracao.repository.decksubtitle

import br.com.fenix.apiIntegracao.model.decksubtitle.Legenda
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

class DeckSubtitleRepository: RepositoryJdbcBase<Legenda, UUID?> {
    override fun update(table: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun insert(table: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun select(table: String, id: UUID?): Optional<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(table: String): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(table: String, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun createTable(table: String) {
        TODO("Not yet implemented")
    }

    override fun existsTable(table: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun tables(): List<String> {
        TODO("Not yet implemented")
    }

    override fun delete(table: String, id: UUID?) {
        TODO("Not yet implemented")
    }

    override fun delete(table: String, obj: Legenda) {
        TODO("Not yet implemented")
    }
}