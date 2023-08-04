package br.com.fenix.apiIntegracao.service

import br.com.fenix.apiIntegracao.model.Atualizacao
import br.com.fenix.apiIntegracao.repository.AtualizacaoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger

@Service
class AtualizacaoService {

    companion object {
        val LOG = Logger.getLogger(AtualizacaoService::class.java.name)
    }

    @Autowired
    private lateinit var repository: AtualizacaoRepository

    fun find(computador: String): Atualizacao {
        return repository.findById(computador)
            .orElse(Atualizacao(UUID.randomUUID().toString(), computador, "", LocalDateTime.now()))
    }

    fun findAll(): List<Atualizacao> {
        return repository.findAll()
    }

    fun create(save: Atualizacao): Atualizacao {
        val entity = repository.findByComputador(save.computador).orElse(save)
        return repository.save(entity)
    }

    fun update(entity: Atualizacao): Atualizacao {
        return repository.save(entity)
    }
}