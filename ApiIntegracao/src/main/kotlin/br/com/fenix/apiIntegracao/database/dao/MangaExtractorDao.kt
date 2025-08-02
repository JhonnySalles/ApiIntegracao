package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.mangaextractor.*
import java.util.*

interface MangaExtractorDao : ExtractorDaoBase<MangaVolume, UUID?> {

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun updateCapa(base: String, obj: MangaCapa): MangaCapa

    @Throws(ExceptionDb::class)
    fun updateCapitulo(base: String, obj: MangaCapitulo): MangaCapitulo

    @Throws(ExceptionDb::class)
    fun updatePagina(base: String, obj: MangaPagina): MangaPagina

    @Throws(ExceptionDb::class)
    fun updateTexto(base: String, obj: MangaTexto): MangaTexto

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun insertCapa(base: String, idVolume: UUID, obj: MangaCapa): UUID?

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: UUID, obj: MangaCapitulo): UUID?

    @Throws(ExceptionDb::class)
    fun insertPagina(base: String, idCapitulo: UUID, obj: MangaPagina): UUID?

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idPagina: UUID, obj: MangaTexto): UUID?

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectCapa(base: String, id: UUID?): Optional<MangaCapa>

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: UUID?): Optional<MangaCapitulo>

    @Throws(ExceptionDb::class)
    fun selectPagina(base: String, id: UUID?): Optional<MangaPagina>

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: UUID?): Optional<MangaTexto>

    @Throws(ExceptionDb::class)
    fun selectAllCapitulos(base: String, idVolume: UUID): List<MangaCapitulo>

    @Throws(ExceptionDb::class)
    fun selectAllPaginas(base: String, idCapitulo: UUID): List<MangaPagina>

    @Throws(ExceptionDb::class)
    fun selectAllTextos(base: String, idPagina: UUID): List<MangaTexto>

    // -------------------------------------------------------------------------------------------------------------  //

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
    fun selectVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null) : MutableSet<MangaVocabulario>

    @Throws(ExceptionDb::class)
    fun insertVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null, vocabulario: Set<MangaVocabulario>)

    @Throws(ExceptionDb::class)
    fun deleteVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, idPagina: UUID? = null, transaction : Boolean = true)

}