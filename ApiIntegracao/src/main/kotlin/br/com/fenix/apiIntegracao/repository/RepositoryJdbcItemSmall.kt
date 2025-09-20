package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.exceptions.ExceptionDb
import br.com.fenix.apiintegracao.model.EntityBase
import java.util.*

interface RepositoryJdbcItemSmall<E : EntityBase<ID, E>, ID> {

    @Throws(ExceptionDb::class)
    fun select(tabela: String, id: ID): Optional<E>

    @Throws(ExceptionDb::class)
    fun update(tabela: String, obj: E): E

    @Throws(ExceptionDb::class)
    fun insert(tabela: String, idParent : ID, obj: E): E

    @Throws(ExceptionDb::class)
    fun findAll(tabela: String, idParent : ID): List<E>

    // -------------------------------------------------------------------------------------------------------------  //

    @Throws(ExceptionDb::class)
    fun existstabela(tabela: String): Boolean

}