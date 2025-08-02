package br.com.fenix.apiintegracao.database.dao

import org.springframework.data.domain.*

abstract class PageBase {

    protected fun <E> toPageable(pageable: Pageable, total: Int, list: List<E>) : Page<E> {
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