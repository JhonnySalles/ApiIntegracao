package br.com.fenix.apiintegracao.controller

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

interface ControllerJdbc<ID, E : EntityBase<ID, E>, D : DtoBase<ID>, C : ControllerJdbc<ID, E, D, C>> {
    fun getPage(page: Int, size: Int, direction: String) : Any
    fun getPage(table: String, page: Int, size: Int, direction: String) : Any
    fun getLastSyncPage(updateDate: String, page: Int, size: Int, direction: String) : Any
    fun getLastSyncPage(table: String, updateDate: String, page: Int, size: Int, direction: String) : Any
    fun getOne(id: ID) : Any
    fun getOne(table: String, id: ID) : Any
}