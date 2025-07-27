package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.data.web.PagedResourcesAssembler

interface ControllerJdbc<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbc<ID, E, D, C>> {
    fun getPage(page: Int, size: Int, direction: String, assembler: PagedResourcesAssembler<D>) : Any
    fun getLastSyncPage(updateDate: String, page: Int, size: Int, direction: String, assembler: PagedResourcesAssembler<D>) : Any
    fun getOne(id: ID) : Any
}