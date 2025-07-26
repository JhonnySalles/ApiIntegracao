package br.com.fenix.apiIntegracao.service.api

import br.com.fenix.apiIntegracao.enums.Tipo
import br.com.fenix.apiIntegracao.exceptions.TableNotExistsException
import br.com.fenix.apiIntegracao.model.api.Tabelas
import br.com.fenix.apiIntegracao.repository.api.TabelasRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TabelasService {

    companion object {
        private val oLog = LoggerFactory.getLogger(TabelasService::class.java.name)

        val PROP_URL = "url"
        val PROP_PORTA = "porta"
        val PROP_BASE = "base"
        val PROP_SERVER = "server"
        val PROP_PORT = "port"
        val PROP_USER = "user"
        val PROP_PASSWORD = "password"
    }

    @Autowired
    private lateinit var repository: TabelasRepository

    fun find(base: Tipo): Tabelas {
        return repository.findByBase(base).orElseThrow { TableNotExistsException("Não encontrado a base $base no servidor.") }
    }

    fun findAll(): List<Tabelas> {
        return repository.findAll()
    }

    fun getProperty(base: Tipo) : Properties {
        val tabela = find(base)
        val prop = Properties()
        prop.setProperty(PROP_URL, tabela.url)
        prop.setProperty(PROP_PORTA, tabela.porta)
        prop.setProperty(PROP_BASE, tabela.base)
        prop.setProperty(PROP_SERVER, tabela.url)
        prop.setProperty(PROP_PORT, tabela.porta)
        prop.setProperty(PROP_USER, tabela.username)
        prop.setProperty(PROP_PASSWORD, tabela.password)
        return prop
    }

}