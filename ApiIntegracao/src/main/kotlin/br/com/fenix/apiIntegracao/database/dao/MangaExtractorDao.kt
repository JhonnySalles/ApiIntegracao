package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.mangaextractor.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface MangaExtractorDao {

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(ExceptionDb::class)
    fun updateVolume(base: String, obj: MangaVolume)

    @Throws(ExceptionDb::class)
    fun updateCapa(base: String, obj: MangaCapa)

    @Throws(ExceptionDb::class)
    fun updateCapitulo(base: String, obj: MangaCapitulo)

    @Throws(ExceptionDb::class)
    fun updatePagina(base: String, obj: MangaPagina)

    @Throws(ExceptionDb::class)
    fun updateTexto(base: String, obj: MangaTexto)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun insertVolume(base: String, obj: MangaVolume): UUID?

    @Throws(ExceptionDb::class)
    fun insertCapa(base: String, idVolume: UUID, obj: MangaCapa)

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: UUID, obj: MangaCapitulo): UUID?

    @Throws(ExceptionDb::class)
    fun insertPagina(base: String, idCapitulo: UUID, obj: MangaPagina): UUID?

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idPagina: UUID, obj: MangaTexto): UUID?

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVolume(base: String, id: UUID): MangaVolume?

    @Throws(ExceptionDb::class)
    fun selectCapa(base: String, id: UUID): MangaCapa?

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: UUID): MangaCapitulo?

    @Throws(ExceptionDb::class)
    fun selectPagina(base: String, id: UUID): MangaPagina?

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: UUID): MangaTexto?

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String): List<MangaVolume>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, pageable: Pageable): Page<MangaVolume>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, dateTime: LocalDateTime): List<MangaVolume>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<MangaVolume>

    @Throws(ExceptionDb::class)
    fun selectAllCapitulos(base: String, idVolume: UUID): List<MangaCapitulo>

    @Throws(ExceptionDb::class)
    fun selectAllPaginas(base: String, idCapitulo: UUID): List<MangaPagina>

    @Throws(ExceptionDb::class)
    fun selectAllTextos(base: String, idPagina: UUID): List<MangaTexto>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun deleteVolume(base: String, obj: MangaVolume)

    @Throws(ExceptionDb::class)
    fun deleteCapa(base: String, obj: MangaCapa, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteCapitulo(base: String, obj: MangaCapitulo, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deletePagina(base: String, obj: MangaPagina, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteTexto(base: String, obj: MangaTexto, transaction : Boolean = true)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createTable(nome: String)

    @Throws(ExceptionDb::class)
    fun existTable(nome: String):Boolean

    @get:Throws(ExceptionDb::class)
    val tables: List<String>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null) : MutableSet<MangaVocabulario>

    @Throws(ExceptionDb::class)
    fun insertVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null, vocabulario: Set<MangaVocabulario>)

    @Throws(ExceptionDb::class)
    fun deleteVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null, transaction : Boolean = true)

}