package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface RepositoryDao<ID, E : EntityBase<ID, E>> {

    fun beginTransaction()
    fun commit()
    fun rollBack()

    fun query(@org.intellij.lang.annotations.Language("sql") sql: String, isThrowsNotUpdate : Boolean = true) : String?
    fun query(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>, isThrowsNotUpdate : Boolean = true) : String?
    fun queryEntity(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>): Optional<E>
    fun queryList(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>): List<E>

    fun insert(obj: E) : ID?
    fun update(obj: E, isThrowsNotUpdate : Boolean)
    fun delete(obj: E)
    fun delete(id: ID, column: String = "id")

    fun find(id: ID, column: String = "id"): Optional<E>
    fun find(params: Map<String, Any>): Optional<E>

    fun findAll(): List<E>
    fun findAll(params: Map<String, Any>): List<E>
    fun findAll(pageable: Pageable): Page<E>
    fun findAll(params: Map<String, Any>, pageable: Pageable): Page<E>

}