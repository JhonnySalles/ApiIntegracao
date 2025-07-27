package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.data.web.PagedResourcesAssembler

interface ControllerJdbcTabela<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbcTabela<ID, E, D, C>> : ControllerJdbc<ID, E, D, C> {
    fun getPage(table: String, page: Int, size: Int, direction: String, assembler: PagedResourcesAssembler<D>) : Any
    fun getLastSyncPage(table: String, updateDate: String, page: Int, size: Int, direction: String, assembler: PagedResourcesAssembler<D>) : Any
    fun getOne(table: String, id: ID) : Any
}