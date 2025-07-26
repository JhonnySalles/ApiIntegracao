package br.com.fenix.apiintegracao.service.api

import br.com.fenix.apiintegracao.model.api.Consultas
import br.com.fenix.apiintegracao.repository.api.AtualizacaoRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AtualizacaoService {

    companion object {
        private val oLog = LoggerFactory.getLogger(AtualizacaoService::class.java.name)
    }

    @Autowired
    private lateinit var repository: AtualizacaoRepository

    fun find(computador: String): Consultas {
        return repository.findById(computador)
            .orElse(Consultas(UUID.randomUUID().toString(), computador, "", LocalDateTime.now()))
    }

    fun findAll(): List<Consultas> {
        return repository.findAll()
    }

    fun create(save: Consultas): Consultas {
        val entity = repository.findByComputador(save.computador).orElse(save)
        return repository.save(entity)
    }

    fun update(entity: Consultas): Consultas {
        return repository.save(entity)
    }
}