package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.novelextractor.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface ExtractorDaoBase<E : EntityBase<ID, E>, ID> {

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun updateVolume(base: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun insertVolume(base: String, obj: E): ID

    @Throws(ExceptionDb::class)
    fun selectVolume(base: String, id: ID): Optional<E>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String): List<E>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, pageable: Pageable): Page<E>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, dateTime: LocalDateTime): List<E>

    @Throws(ExceptionDb::class)
    fun selectAllVolumes(base: String, dateTime: LocalDateTime, pageable: Pageable): Page<E>

    @Throws(ExceptionDb::class)
    fun deleteVolume(base: String, obj: E)

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun createTable(nome: String)

    @Throws(ExceptionDb::class)
    fun existTable(nome: String): Boolean

    @get:Throws(ExceptionDb::class)
    val tables: List<String>

}