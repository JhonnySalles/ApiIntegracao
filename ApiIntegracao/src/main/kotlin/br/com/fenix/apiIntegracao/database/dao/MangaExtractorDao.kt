package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.mangaextractor.*
import java.util.*

interface MangaExtractorDao {

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(ExceptionDb::class)
    fun updateVolume(base: String, obj: Volume)

    @Throws(ExceptionDb::class)
    fun updateCapitulo(base: String, obj: Capitulo)

    @Throws(ExceptionDb::class)
    fun updatePagina(base: String, obj: Pagina)

    @Throws(ExceptionDb::class)
    fun updateTexto(base: String, obj: Texto)

    @Throws(ExceptionDb::class)
    fun updateVocabulario(base: String, obj: Vocabulario)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun insertVolume(base: String, obj: Volume): UUID?

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: UUID, obj: Capitulo): UUID?

    @Throws(ExceptionDb::class)
    fun insertPagina(base: String, idCapitulo: UUID, obj: Pagina): UUID?

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idPagina: UUID, obj: Texto): UUID?

    @Throws(ExceptionDb::class)
    fun insertVocabulario(
        base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, vocabulario: Set<Vocabulario>
    )

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVolume(base: String, id: UUID): Volume?

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: UUID): Capitulo?

    @Throws(ExceptionDb::class)
    fun selectPagina(base: String, id: UUID): Pagina?

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: UUID): Texto?

    @Throws(ExceptionDb::class)
    fun selectVocabulario(base: String, id: UUID): Vocabulario?

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String): List<Volume>

    @Throws(ExceptionDb::class)
    fun selectAllCapitulos(base: String, idVolume: UUID): List<Capitulo>

    @Throws(ExceptionDb::class)
    fun selectAllPaginas(base: String, idCapitulo: UUID): List<Pagina>

    @Throws(ExceptionDb::class)
    fun selectAllTextos(base: String, idPagina: UUID): List<Texto>

    @Throws(ExceptionDb::class)
    fun selectAllVocabularios(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, comObj : Boolean = false): Set<Vocabulario>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun deleteVolume(base: String, obj: Volume)

    @Throws(ExceptionDb::class)
    fun deleteCapitulo(base: String, obj: Capitulo, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deletePagina(base: String, obj: Pagina, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteTexto(base: String, obj: Texto, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteVocabulario(base: String, idVolume: UUID?, idCapitulo: UUID?, idPagina: UUID?, transaction : Boolean = true)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createTable(nome: String)

    @Throws(ExceptionDb::class)
    fun existTable(nome: String):Boolean

    @get:Throws(ExceptionDb::class)
    val tables: List<String>
}