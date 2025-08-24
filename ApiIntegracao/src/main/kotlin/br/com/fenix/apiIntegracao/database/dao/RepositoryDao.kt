package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.enums.Igualdade
import br.com.fenix.apiintegracao.model.Condicao
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

    fun insert(obj: E, isThrowsNotInsert : Boolean) : ID?
    fun update(obj: E, isThrowsNotUpdate : Boolean)
    fun delete(obj: E, isThrowsNotDelete : Boolean)
    fun delete(id: ID, column: String = "id")

    fun find(id: ID, column: String = "id"): Optional<E>
    fun find(id: ID, column: String = "id", igualdade: Igualdade): Optional<E>
    fun find(params: Map<String, Condicao>): Optional<E>

    fun findAll(): List<E>
    fun findAll(params: Map<String, Condicao>): List<E>
    fun findAll(pageable: Pageable): Page<E>
    fun findAll(params: Map<String, Condicao>, pageable: Pageable): Page<E>

}