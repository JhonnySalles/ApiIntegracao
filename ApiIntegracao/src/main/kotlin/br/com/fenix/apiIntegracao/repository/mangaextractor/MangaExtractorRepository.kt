package br.com.fenix.apiIntegracao.repository.mangaextractor

import br.com.fenix.apiIntegracao.database.dao.DaoFactory
import br.com.fenix.apiIntegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiIntegracao.enums.Tipo
import br.com.fenix.apiIntegracao.exceptions.RequiredObjectIsNullException
import br.com.fenix.apiIntegracao.model.mangaextractor.Volume
import br.com.fenix.apiIntegracao.repository.RepositoryJdbcBase
import br.com.fenix.apiIntegracao.service.api.TabelasService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.thread

@Repository
class MangaExtractorRepository(var tabelas : TabelasService): RepositoryJdbcBase<Volume, UUID?> {

    private val dao : MangaExtractorDao = DaoFactory.createMangaExtractorDao(tabelas.getProperty(Tipo.MANGAEXTRACTOR))

    override fun createtabela(tabela: String) {
        dao.createTable(tabela)
    }

    override fun existstabela(tabela: String): Boolean {
        return dao.existTable(tabela)
    }

    override fun tabelas(): List<String> {
        return dao.tables
    }

    override fun update(tabela: String, obj: Volume): Volume {
        dao.updateVolume(tabela, obj)
        for(capitulo in obj.capitulos) {
            if (capitulo.getId() == null)
                capitulo.setId(dao.insertCapitulo(tabela, obj.getId()!!, capitulo))
            else
                dao.updateCapitulo(tabela, capitulo)

            for (pagina in capitulo.paginas) {
                if (capitulo.getId() == null)
                    pagina.setId(dao.insertPagina(tabela, capitulo.getId()!!, pagina))
                else
                    dao.updatePagina(tabela, pagina)

                for (texto in pagina.textos)
                    if (capitulo.getId() == null)
                        pagina.setId(dao.insertTexto(tabela, pagina.getId()!!, texto))
                    else
                        dao.updateTexto(tabela, texto)
            }
        }
        return obj
    }

    override fun insert(tabela: String, obj: Volume): Volume {
        obj.setId(dao.insertVolume(tabela, obj))
        for(capitulo in obj.capitulos) {
            capitulo.setId(dao.insertCapitulo(tabela, obj.getId()!!, capitulo))
            for (pagina in capitulo.paginas) {
                pagina.setId(dao.insertPagina(tabela, capitulo.getId()!!, pagina))
                for (texto in pagina.textos)
                    pagina.setId(dao.insertTexto(tabela, pagina.getId()!!, texto))
            }
        }
        return obj
    }

    override fun select(tabela: String, id: UUID?): Optional<Volume> {
        if (id == null)
            return Optional.empty()

        return Optional.ofNullable(dao.selectVolume(tabela, id))
    }

    override fun findAll(tabela: String): List<Volume> {
        return dao.selectAllVolumes(tabela)
    }

    override fun findAll(tabela: String, pageable: Pageable?): Page<Volume> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAllVolumes(tabela, pageable)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime): List<Volume> {
        return dao.selectAllVolumes(tabela, dateTime)
    }

    override fun findAllByAtualizacaoGreaterThanEqual(tabela: String, dateTime: LocalDateTime, pageable: Pageable?): Page<Volume> {
        if (pageable == null)
            throw RequiredObjectIsNullException("Its necessary inform a pageable")

        return dao.selectAllVolumes(tabela, dateTime, pageable)
    }

    override fun delete(tabela: String, id: UUID?) {
        select(tabela, id).ifPresent { dao.deleteVolume(tabela, it) }
    }

    override fun delete(tabela: String, obj: Volume) {
        dao.deleteVolume(tabela, obj)
    }
}