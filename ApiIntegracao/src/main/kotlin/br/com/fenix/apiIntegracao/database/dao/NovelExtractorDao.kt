package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.novelextractor.*
import java.util.*

interface NovelExtractorDao : ExtractorDaoBase<NovelVolume, UUID?> {

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun updateCapa(base: String, obj: NovelCapa): NovelCapa

    @Throws(ExceptionDb::class)
    fun updateCapitulo(base: String, obj: NovelCapitulo): NovelCapitulo

    @Throws(ExceptionDb::class)
    fun updateTexto(base: String, obj: NovelTexto): NovelTexto

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun insertCapa(base: String, idVolume: UUID, obj: NovelCapa): UUID?

    @Throws(ExceptionDb::class)
    fun insertCapitulo(base: String, idVolume: UUID, obj: NovelCapitulo): UUID?

    @Throws(ExceptionDb::class)
    fun insertTexto(base: String, idCapitulo: UUID, obj: NovelTexto): UUID?

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectCapa(base: String, id: UUID?): Optional<NovelCapa>

    @Throws(ExceptionDb::class)
    fun selectCapitulo(base: String, id: UUID?): Optional<NovelCapitulo>

    @Throws(ExceptionDb::class)
    fun selectTexto(base: String, id: UUID?): Optional<NovelTexto>

    @Throws(ExceptionDb::class)
    fun selectAllCapitulos(base: String, idVolume: UUID): List<NovelCapitulo>

    @Throws(ExceptionDb::class)
    fun selectAllTextos(base: String, idCapitulo: UUID): List<NovelTexto>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun deleteCapa(base: String, obj: NovelCapa, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteCapitulo(base: String, obj: NovelCapitulo, transaction : Boolean = true)

    @Throws(ExceptionDb::class)
    fun deleteTexto(base: String, obj: NovelTexto, transaction : Boolean = true)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun selectVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null) : MutableSet<NovelVocabulario>

    @Throws(ExceptionDb::class)
    fun insertVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, vocabulario: Set<NovelVocabulario>)

    @Throws(ExceptionDb::class)
    fun deleteVocabulario(base: String, idVolume: UUID? = null, idCapitulo: UUID? = null, transaction : Boolean = true)

}