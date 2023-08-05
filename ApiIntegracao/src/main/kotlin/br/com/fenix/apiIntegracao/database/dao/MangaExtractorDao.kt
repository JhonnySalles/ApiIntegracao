package br.com.fenix.apiIntegracao.database.dao

import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.model.mangaextractor.*

interface MangaExtractorDao {

    // -------------------------------------------------------------------------------------------------------------  //
    @Throws(ExceptionDb::class)
    fun updateVolume(base: String, obj: Volumes)

    @Throws(ExceptionDb::class)
    fun updateCapitulo(base: String, obj: Capitulos)

    @Throws(ExceptionDb::class)
    fun updatePagina(base: String, obj: Paginas)

    @Throws(ExceptionDb::class)
    fun updateTexto(base: String, obj: Textos)

    @Throws(ExceptionDb::class)
    fun updateVocabulario(base: String, obj: Vocabulario)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun insertVolume(base: String, obj: Volumes): Long

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: Long, obj: Capitulos): Long

    @Throws(ExceptionDb::class)
    fun insertPagina(base: String, idCapitulo: Long, obj: Paginas): Long

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idPagina: Long, obj: Textos): Long

    @Throws(ExceptionDb::class)
    fun insertVocabulario(
        base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?, vocabulario: Set<Vocabulario>
    )

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVolume(base: String, id: Long): Volumes

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: Long): Capitulos

    @Throws(ExceptionDb::class)
    fun selectPagina(base: String, id: Long): Paginas

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: Long): Textos

    @Throws(ExceptionDb::class)
    fun selectVocabuario(base: String, id: Long): Vocabulario

    @Throws(ExceptionDb::class)
    fun selectAllCapitulo(base: String, idVolume: Long): List<Capitulos>

    @Throws(ExceptionDb::class)
    fun selectAllPagina(base: String, idCapitulo: Long): List<Vocabulario>

    @Throws(ExceptionDb::class)
    fun selectAllText(base: String, idPagina: Long): List<Paginas>

    @Throws(ExceptionDb::class)
    fun selectAllVocabuario(base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?): Set<Vocabulario>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun deleteVolume(base: String, obj: Volumes)

    @Throws(ExceptionDb::class)
    fun deleteCapitulo(base: String, obj: Capitulos)

    @Throws(ExceptionDb::class)
    fun deletePagina(base: String, obj: Paginas)

    @Throws(ExceptionDb::class)
    fun deleteTexto(base: String, obj: Textos)

    @Throws(ExceptionDb::class)
    fun deletarVocabulario(base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createDatabase(nome: String)

    @Throws(ExceptionDb::class)
    fun existDatabase(nome: String):Boolean

    @Throws(ExceptionDb::class)
    fun selectAllTabela() : List<Tabelas>

    @get:Throws(ExceptionDb::class)
    val tabelas: List<String>
}