package br.com.fenix.apiIntegracao.repository.mangaextractor

import br.com.fenix.apiIntegracao.model.mangaextractor.Volume
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

class MangaExtractorRepository: RepositoryJdbcBase<Volume, UUID?> {
    override fun update(table: String, obj: Volume): Volume {
        TODO("Not yet implemented")
    }

    override fun insert(table: String, obj: Volume): Volume {
        TODO("Not yet implemented")
    }

    override fun select(table: String, id: UUID?): Optional<Volume> {
        TODO("Not yet implemented")
    }

    override fun findAll(table: String): List<Volume> {
        TODO("Not yet implemented")
    }

    override fun findAll(table: String, pageable: Pageable?): Page<Volume> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime): List<Volume> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(table: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Volume> {
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

    override fun delete(table: String, obj: Volume) {
        TODO("Not yet implemented")
    }
}