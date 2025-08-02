package br.com.fenix.apiintegracao.repository.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.database.DaoFactory
import br.com.fenix.apiintegracao.database.dao.DeckSubtitleDao
import br.com.fenix.apiintegracao.database.dao.MangaExtractorDao
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
class MangaExtractorRepository(private val registry : DynamicJdbcRegistry): RepositoryJdbcTabela<Legenda, UUID?> {

    private val dao : MangaExtractorDao  by lazy { DaoFactory.createMangaExtractorDao(registry.getSource(Conexao.MANGA_EXTRACTOR)) }

    override fun update(obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun update(tabela: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun insert(tabela: String, obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun insert(obj: Legenda): Legenda {
        TODO("Not yet implemented")
    }

    override fun select(tabela: String, id: UUID?): Optional<Legenda> {
        TODO("Not yet implemented")
    }

    override fun select(id: UUID?): Optional<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(tabela: String): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(tabela: String, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime): List<Legenda> {
        TODO("Not yet implemented")
    }

    override fun findAllByAtualizacaoGreaterThanEqual(dateTime: LocalDateTime, pageable: Pageable?): Page<Legenda> {
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

    override fun delete(id: UUID?) {
        TODO("Not yet implemented")
    }

    override fun delete(obj: Legenda) {
        TODO("Not yet implemented")
    }

    override fun delete(tabela: String, id: UUID?) {
        TODO("Not yet implemented")
    }

    override fun delete(tabela: String, obj: Legenda) {
        TODO("Not yet implemented")
    }


}