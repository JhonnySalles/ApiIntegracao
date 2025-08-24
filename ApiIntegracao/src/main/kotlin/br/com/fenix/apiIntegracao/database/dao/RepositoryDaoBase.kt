package br.com.fenix.apiintegracao.database.dao

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.enums.Igualdade
import br.com.fenix.apiintegracao.messages.Mensagens
import br.com.fenix.apiintegracao.model.Condicao
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.utils.Utils
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.Date

abstract class RepositoryDaoBase<ID, E : EntityBase<ID, E>>(conexao: Connection) : RepositoryDao<ID, E> {

    private val clazzEntity: Class<E>

    init {
        val superclass = (this.javaClass.genericSuperclass as ParameterizedType)
        clazzEntity = superclass.actualTypeArguments[1] as Class<E>
    }

    companion object {
        private val oLog: Logger = LoggerFactory.getLogger(RepositoryDaoBase::class.java)
    }

    protected val conn: Connection = conexao

    private val INSERT : String = "INSERT INTO %s (%s) VALUES (%s)"
    private val UPDATE : String = "UPDATE %s SET %s WHERE %s"
    private val DELETE : String = "DELETE FROM %s WHERE %s"
    private val SELECT : String = "SELECT %s FROM %s "
    private val WHERE : String = " WHERE %s "
    private val ORDER : String = " ORDER BY %s "
    private val PAGE : String = " LIMIT %d,%d"
    private val SELECT_COUNT = "SELECT count(*) as total FROM %s WHERE %s"

    abstract fun toEntity(rs: ResultSet): E

    abstract fun toID(id : String?) : ID

    /**
     * Inicia a transação no banco de dados
     */
    override fun beginTransaction() = conn.beginRequest()

    /**
     * Confirma as altrações no banco
     */
    override fun commit() = conn.commit()

    /**
     * Desfaz as alterações no banco
     */
    override fun rollBack() = conn.rollback()

    private fun prepareSql(sql: String) : String = sql.replace(":[\\w]*".toRegex(), "?")

    /**
     * Função chamada quando o parâmetro padrão não é encontrado, no qual se espera um retorno em string
     * @param param parâmetro não identificado.
     * @return retorno em string para salvamento do parâmetro
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    abstract fun getCustomParam(param : Objects) : String

    private fun setParams(st: PreparedStatement, params: Map<String, Any?>) {
        var index = 0
        for (p in params.keys) {
            if (params[p] == null)
                st.setNull(++index, Types.NULL)
            else
                when (params[p]) {
                    is Int -> st.setInt(++index, params[p] as Int)
                    is BigDecimal -> st.setBigDecimal(++index, params[p] as BigDecimal)
                    is Long -> st.setLong(++index, params[p] as Long)
                    is Float -> st.setFloat(++index, params[p] as Float)
                    is Double -> st.setDouble(++index, params[p] as Double)
                    is String -> st.setString(++index, params[p] as String)
                    is Boolean -> st.setBoolean(++index, params[p] as Boolean)
                    is UUID -> st.setString(++index, params[p].toString())
                    is Timestamp -> st.setTimestamp(++index, params[p] as Timestamp)
                    is Time -> st.setTime(++index, params[p] as Time)
                    is Date -> st.setDate(++index, java.sql.Date((params[p] as Date).time))
                    is LocalDateTime -> st.setString(++index, Utils.convertToString(params[p] as LocalDateTime))
                    is LocalDate -> st.setString(++index, Utils.convertToString(params[p] as LocalDate))
                    is LocalTime -> st.setString(++index, (params[p] as LocalTime).toString())
                    is Enum<*> -> st.setString(++index, (params[p] as Enum<*>).name)
                    is Blob -> st.setBlob(++index, params[p] as Blob)
                    is Objects -> st.setString(++index, getCustomParam(params[p] as Objects))
                }
        }
    }

    private fun toParams(params: Map<String, Condicao?>) = params.map { it.key to it.value?.valor }.toMap()

    /**
     * Consulta a quantidade de registros em uma tabela e retorna a quantidade
     * @param table tabela em string
     * @param sql parametros da consulta
     * @return a quantidade de registros existentes
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    private fun count(table: String, params: Map<String, Condicao?>): Int {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            var condicao = "1>0 AND "
            for (param in params)
                condicao += "${param.key} ${(param.value?.igualdade ?: Igualdade.IGUAL).valor} ? AND "

            st = conn.prepareStatement(String.format(SELECT_COUNT, table, condicao.substringBeforeLast(" AND ")))
            if (params.isNotEmpty())
                setParams(st, toParams(params))

            rs = st.executeQuery()
            return if (rs.next())
                rs.getInt(1)
            else
                0
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
            DynamicJdbcRegistry.closeResultSet(rs)
        }
    }

    /**
     * Executa uma query no banco, no qual poderá ser um insert ou update
     * @param sql query em string para o java
     * @param isThrowsNotUpdate indica se deve lançar excessão em não atualização (padrão true)
     * @return retorna uma string da key gerada ou null se não localizado
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun query(@org.intellij.lang.annotations.Language("sql") sql: String, isThrowsNotUpdate : Boolean) : String? {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                oLog.info(sql)
                if (isThrowsNotUpdate)
                    throw SQLException(Mensagens.BD_ERRO_INSERT_OR_UPDATE)
            }

            val rs = st.generatedKeys
            return if (rs.next())
                rs.getString(1)
            else
                null
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            oLog.info(sql)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
        }
    }

    /**
     * Executa uma query no banco com parâmetros
     * @param sql query em string
     * @param params parametros da consulta
     * @param isThrowsNotUpdate indica se deve lançar excessão em não atualização (padrão true)
     * @return retorna uma string da key gerada ou null se não localizado
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun query(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>, isThrowsNotUpdate : Boolean) : String? {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(prepareSql(sql), Statement.RETURN_GENERATED_KEYS)
            setParams(st, params)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                oLog.info(sql)
                if (isThrowsNotUpdate)
                    throw SQLException(Mensagens.BD_ERRO_INSERT_OR_UPDATE)
            }

            val rs = st.generatedKeys
            return if (rs.next())
                rs.getString(1)
            else
                null
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
        }
    }

    /**
     * Executa uma query no banco com parâmetros e retorno
     * @param sql query em string
     * @param sql parametros da consulta
     * @return uma lista com os objetos encontrado
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun queryEntity(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>): Optional<E> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(prepareSql(sql))
            setParams(st, params)
            rs = st.executeQuery()
            if (rs.next())
                Optional.of(toEntity(rs))
            else
                Optional.empty<E>()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
            DynamicJdbcRegistry.closeResultSet(rs)
        }
    }

    /**
     * Executa uma query no banco com parâmetros e retorno
     * @param sql query em string
     * @param sql parametros da consulta
     * @return uma lista com os objetos encontrado
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun queryList(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any?>): List<E> {
        var st: PreparedStatement? = null
        var rs: ResultSet? = null
        return try {
            st = conn.prepareStatement(prepareSql(sql))
            setParams(st, params)
            rs = st.executeQuery()
            val list: MutableList<E> = mutableListOf()
            while (rs.next())
                list.add(toEntity(rs))
            list
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
            DynamicJdbcRegistry.closeResultSet(rs)
        }
    }

    /**
     * Executa uma query nativa no sistema
     * @param sql query em string para o java persistem
     * @throws SQLException caso o sql esteja errado
     */
    @Transactional
    open fun queryNative(@org.intellij.lang.annotations.Language("sql") sql: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(sql)
            st.executeUpdate()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
        }
    }

    /**
     * Executa uma query nativa com parâmetros
     * @param sql query em string para o java persistem
     * @throws SQLException caso o sql esteja errado
     */
    @Transactional
    open fun queryNative(@org.intellij.lang.annotations.Language("sql") sql: String, params: Map<String, Any>) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(prepareSql(sql))
            setParams(st, params)
            st.executeUpdate()
        } catch (e: SQLException) {
            oLog.error(e.message, e)
            throw SQLException(Mensagens.BD_ERRO_QUERRY)
        } finally {
            DynamicJdbcRegistry.closeStatement(st)
        }
    }

    private fun getParametros(obj: E, isWithoutId: Boolean = false) : Map<String, Any?> {
        val paramns = mutableMapOf<String, Any?>()
        val fields = clazzEntity.declaredFields
        for (field in fields) {
            if (field.isAnnotationPresent(Column::class.java)) {
                if (isWithoutId && field.isAnnotationPresent(Id::class.java))
                    continue

                val annotation = field.getAnnotation(Column::class.java)
                field.isAccessible = true
                val name = annotation.name.ifEmpty { field.name }
                paramns[name] = field.get(obj)
            }
        }
        return paramns
    }

    private fun getIds(obj : E) : Map<String, Any?> {
        val ids = mutableMapOf<String, Any?>()
        val fields = clazzEntity.declaredFields
        for (field in fields) {
            if (field.isAnnotationPresent(Id::class.java)) {
                val annotation = field.getAnnotation(Column::class.java)
                field.isAccessible = true
                ids[annotation.name] = field.get(obj)
            }
        }
        return ids
    }

    private fun getTabela(obj : E) : String {
        val annotation = clazzEntity.getAnnotation(Table::class.java)
        return annotation.name
    }

    /**
     * Insere um objeto no banco de dados, use @Column para identificar quais campos são salvo, @Table para tabela e @Id para os ids.
     * @param obj query em string
     * @return retorna um id do registro gerado
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun insert(obj: E, isThrowsNotInsert: Boolean) : ID? {
        val parametros = getParametros(obj)
        var colunas = ""
        var valores = ""
        for (param in parametros.keys) {
            colunas += "$param,"
            valores += "?,"
        }

        val sql = String.format(INSERT, getTabela(obj), colunas.substringBeforeLast(","), valores.substringBeforeLast(","))
        //LOGGER.info("Gerado SQL Insert: $sql")
        val generate = query(sql, getParametros(obj))
        val id = getIds(obj).values.first()

        return if (id != null)
                id as ID
            else if (generate != null)
                toID(generate)
            else if (isThrowsNotInsert)
                throw SQLException(Mensagens.BD_ERRO_INSERT)
            else
                null
    }

    /**
     * Atualiza um objeto no banco de dados, use @Column para identificar quais campos são salvo, @Table para tabela e @Id para os ids.
     * @param obj query em string
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun update(obj: E, isThrowsNotUpdate : Boolean) {
        val parametros = getParametros(obj, true).toMutableMap()
        var colunas = ""
        for (param in parametros.keys)
            colunas += "$param = ?,"

        val ids = getIds(obj)
        var chaves = ""
        for (param in ids.keys) {
            chaves += "$param = ? AND "
            parametros["id_$param"] = ids[param]
        }

        val sql = String.format(UPDATE, getTabela(obj), colunas.substringBeforeLast(","), chaves.substringBeforeLast(" AND "))
        //LOGGER.info("Gerado SQL Update: $sql")
        try {
            toID(query(sql, parametros, isThrowsNotUpdate))
        } catch (e : Exception) {
            if (isThrowsNotUpdate)
                throw SQLException(Mensagens.BD_ERRO_UPDATE, e)
        }
    }

    /**
     * Deleta um objeto no banco de dados pelo id, caso não informmado será utilizado pela tag @Id.
     * @param id id do registro a ser excluido
     * @param column campo no banco referente ao id, padrão (id)
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun delete(obj: E, isThrowsNotDelete: Boolean) {
        val condicao = mutableMapOf<String, Any?>()
        val ids = getIds(obj)
        var chaves = ""
        for (param in ids.keys) {
            chaves += "$param = ? AND "
            condicao["id_$param"] = ids[param]
        }

        val sql = String.format(DELETE, getTabela(obj), chaves.substringBeforeLast(" AND "))
        //LOGGER.info("Gerado SQL Delete: $sql")

        try {
            query(sql, condicao, isThrowsNotDelete)
        } catch (e : Exception) {
            if (isThrowsNotDelete)
                throw SQLException(Mensagens.BD_ERRO_DELETE, e)
        }
    }

    /**
     * Deleta um objeto no banco de dados pelo id, caso não informmado será utilizado pela tag @Id.
     * @param id id do registro a ser excluido
     * @param column campo no banco referente ao id, padrão (id)
     * @throws SQLException caso o sql esteja errado ou não tenha nenhuma linha alterada
     */
    override fun delete(id: ID, column: String) {
        val entity = clazzEntity.newInstance()
        var condicao = Pair(column, id)
        var chave = "$column = ?"
        if (column.isEmpty()) {
            val ids = getIds(entity)
            val campo = ids.keys.first()
            chave = "$campo = ?"
            condicao = Pair(campo, id)
        }
        val sql = String.format(DELETE, getTabela(entity), chave)
        //LOGGER.info("Gerado SQL Delete: $sql")
        query(sql, mapOf(condicao))
    }

    /**
     * Localiza um registro e retorna o objeto selecionado, caso não informmado será utilizado pela tag @Id.
     * @param id id do objeto
     * @param column campo no banco referente ao id, padrão (id)
     * @return retorna um objeto se encontrado
     * @throws SQLException caso o sql esteja errado
     */
    override fun find(id: ID, column: String): Optional<E> = find(id, column, Igualdade.IGUAL)

    /**
     * Localiza um registro e retorna o objeto selecionado, caso não informmado será utilizado pela tag @Id.
     * @param id id do objeto
     * @param column campo no banco referente ao id, padrão (id)
     * @param igualdade campo no banco referente a condição para a consulta da coluna e id
     * @return retorna um objeto se encontrado
     * @throws SQLException caso o sql esteja errado
     */
    override fun find(id: ID, column: String, igualdade: Igualdade): Optional<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        var condicao = Pair(column, id)
        var chave = "$column ${igualdade.valor} ?"
        if (column.isEmpty()) {
            val ids = getIds(entity)
            val campo = ids.keys.first()
            chave = "$campo ${igualdade.valor} ?"
            condicao = Pair(campo, id)
        }

        val sql = String.format(SELECT + WHERE, campos.substringBeforeLast(","), getTabela(entity), chave)
        //LOGGER.info("Gerado SQL Select By Id: $sql")
        return queryEntity(sql, mapOf(condicao))
    }

    /**
     * Localiza um registro e retorna o objeto selecionado, caso não informmado será utilizado pela tag @Id.
     * @param params lista de campos para a pesquisa
     * @return retorna um objeto se encontrado
     * @throws SQLException caso o sql esteja errado
     */
    override fun find(params: Map<String, Condicao>): Optional<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        var condicao = "1>0 AND "
        for (column in params)
            condicao += "${column.key} ${(column.value.igualdade ?: Igualdade.IGUAL).valor} ? AND "

        val sql = String.format(SELECT + WHERE, campos.substringBeforeLast(","), getTabela(entity), condicao.substringBeforeLast(" AND "))
        //LOGGER.info("Gerado SQL Select: $sql")
        return queryEntity(sql, toParams(params))
    }

    /**
     * Localiza um registro e retorna o objeto selecionado, caso não informmado será utilizado pela tag @Id.
     * @param params parametros do objeto a ser localizado
     * @return retorna um objeto se encontrado
     * @throws SQLException caso o sql esteja errado
     */
    override fun findAll(params: Map<String, Condicao>): List<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        var condicao = "1>0 AND "
        for (column in params)
            condicao += "${column.key} ${(column.value.igualdade ?: Igualdade.IGUAL).valor} ? AND "

        val sql = String.format(SELECT + WHERE, campos.substringBeforeLast(","), getTabela(entity), condicao.substringBeforeLast(" AND "))
        //LOGGER.info("Gerado SQL Select: $sql")
        return queryList(sql, toParams(params))
    }

    /**
     * Retorna todos os objetos do banco de dados
     * @return uma lista com os objetos encontrado
     * @throws SQLException caso o sql esteja errado
     */
    override fun findAll(): List<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        val sql = String.format(SELECT, campos.substringBeforeLast(","), getTabela(entity))
        //LOGGER.info("Gerado SQL Select: $sql")
        return queryList(sql, mapOf())
    }

    /**
     * Retorna todos os objetos do banco de dados
     * @param pageable paginação da página para consulta
     * @return uma lista paginada com os objetos encontrados
     * @throws SQLException caso o sql esteja errado
     */
    override fun findAll(pageable: Pageable): Page<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        val tabela = getTabela(entity)
        val sql = if (!pageable.sort.isEmpty) {
            var order = ""
            for (sort in pageable.sort)
                order += "${sort.property} ${if (sort.direction.isAscending) "ASC" else "DESC"}, "

            if (order.trim().isEmpty())
                order = "1,"

            String.format(SELECT + ORDER + PAGE, campos.substringBeforeLast(","), tabela, order.substringBeforeLast(","), pageable.pageNumber, pageable.pageSize)
        } else
            String.format(SELECT + PAGE, campos.substringBeforeLast(","), tabela, pageable.pageNumber, pageable.pageSize)

        //LOGGER.info("Gerado SQL Select: $sql")
        return toPageable(pageable, count(tabela, mapOf()), queryList(sql, mapOf()))
    }

    /**
     * Retorna todos os objetos do banco de dados
     * @param params parametros do objeto a ser localizado
     * @param pageable paginação da página para consulta
     * @return uma lista paginada com os objetos encontrados
     * @throws SQLException caso o sql esteja errado
     */
    override fun findAll(params: Map<String, Condicao>, pageable: Pageable): Page<E> {
        val entity = clazzEntity.newInstance()
        val parametros = getParametros(entity)
        var campos = ""
        for (param in parametros.keys)
            campos += "$param,"

        var condicao = "1>0 AND "
        for (param in params)
            condicao += "${param.key} ${(param.value.igualdade ?: Igualdade.IGUAL).valor} ? AND "

        val tabela = getTabela(entity)
        val sql = if (!pageable.sort.isEmpty) {
            var order = ""
            for (sort in pageable.sort)
                order += "${sort.property} ${if (sort.direction.isAscending) "ASC" else "DESC"}, "

            if (order.trim().isEmpty())
                order = "1,"

            String.format(SELECT + WHERE + ORDER + PAGE, campos.substringBeforeLast(","), tabela, condicao.substringBeforeLast(" AND "), order.substringBeforeLast(","), pageable.pageNumber, pageable.pageSize)
        } else
            String.format(SELECT + WHERE + PAGE, campos.substringBeforeLast(","), tabela, condicao.substringBeforeLast(" AND "), pageable.pageNumber, pageable.pageSize)

        //LOGGER.info("Gerado SQL Select: $sql")
        return toPageable(pageable, count(tabela, params), queryList(sql, toParams(params)))
    }

    private fun <E> toPageable(pageable: Pageable, total: Int, list: List<E>) : Page<E> {
        val page: Pageable = object : Pageable {
            override fun getPageNumber(): Int {
                return pageable.pageNumber
            }

            override fun getPageSize(): Int {
                return pageable.pageSize
            }

            override fun getOffset(): Long {
                return pageable.pageNumber * pageable.pageSize.toLong()
            }

            override fun getSort(): Sort {
                return pageable.sort
            }

            override fun next(): Pageable {
                return PageRequest.of(this.pageNumber + 1, this.pageSize, this.sort)
            }

            override fun previousOrFirst(): Pageable {
                return if (this.pageNumber == 0) this else PageRequest.of(this.pageNumber - 1, this.pageSize, this.sort)
            }

            override fun first(): Pageable {
                return PageRequest.of(0, this.pageSize, this.sort)
            }

            override fun withPage(pageNumber: Int): Pageable {
                TODO("Not yet implemented")
            }

            override fun hasPrevious(): Boolean {
                return pageable.pageNumber > 0
            }
        }

        return PageImpl(list, page, total.toLong())
    }

}