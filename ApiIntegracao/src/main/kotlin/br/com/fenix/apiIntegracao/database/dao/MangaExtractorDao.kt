package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.mangaextractor.*

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
    fun insertVolume(base: String, obj: Volume): Long

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: Long, obj: Capitulo): Long

    @Throws(ExceptionDb::class)
    fun insertPagina(base: String, idCapitulo: Long, obj: Pagina): Long

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idPagina: Long, obj: Texto): Long

    @Throws(ExceptionDb::class)
    fun insertVocabulario(
        base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?, vocabulario: Set<Vocabulario>
    )

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVolume(base: String, id: Long): Volume?

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: Long): Capitulo?

    @Throws(ExceptionDb::class)
    fun selectPagina(base: String, id: Long): Pagina?

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: Long): Texto?

    @Throws(ExceptionDb::class)
    fun selectVocabulario(base: String, id: Long): Vocabulario?

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String): List<Volume>

    @Throws(ExceptionDb::class)
    fun selectAllCapitulos(base: String, idVolume: Long): List<Capitulo>

    @Throws(ExceptionDb::class)
    fun selectAllPaginas(base: String, idCapitulo: Long): List<Pagina>

    @Throws(ExceptionDb::class)
    fun selectAllTextos(base: String, idPagina: Long): List<Texto>

    @Throws(ExceptionDb::class)
    fun selectAllVocabularios(base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?): Set<Vocabulario>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun deleteVolume(base: String, obj: Volume)

    @Throws(ExceptionDb::class)
    fun deleteCapitulo(base: String, obj: Capitulo)

    @Throws(ExceptionDb::class)
    fun deletePagina(base: String, obj: Pagina)

    @Throws(ExceptionDb::class)
    fun deleteTexto(base: String, obj: Texto)

    @Throws(ExceptionDb::class)
    fun deleteVocabulario(base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createDatabase(nome: String)

    @Throws(ExceptionDb::class)
    fun existDatabase(nome: String):Boolean

    @Throws(ExceptionDb::class)
    fun selectAllTabelas() : List<Tabela>

    @get:Throws(ExceptionDb::class)
    val tabelas: List<String>
}