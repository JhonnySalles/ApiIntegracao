package br.com.fenix.apiIntegracao.mapper

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component


@Component
class Mapper {
    companion object {
        @Autowired
        lateinit var mapper: ModelMapper

        fun <O, D> parse(origin: O, destination: Class<D>): D = mapper.map(origin, destination)

        fun <O, D> parse(origin: List<O>, destination: Class<D>): List<D> = origin.map { mapper.map(it, destination) }

        fun <O, D> parse(origin: Page<O>, destination: Class<D>): Page<D> = origin.map { mapper.map(it, destination) }
    }
}