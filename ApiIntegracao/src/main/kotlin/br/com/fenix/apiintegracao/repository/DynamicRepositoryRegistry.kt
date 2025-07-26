package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.enums.Conexao
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class DynamicRepositoryRegistry {
    private val repositoriesByConnection = ConcurrentHashMap<Conexao, MutableMap<Class<*>, Any>>()
    private val entityManagersByConnection = ConcurrentHashMap<Conexao, jakarta.persistence.EntityManager>()

    fun register(conexao: Conexao, repositoryInterface: Class<*>, repositoryInstance: Any) {
        val connectionRepos = repositoriesByConnection.computeIfAbsent(conexao) { ConcurrentHashMap() }
        connectionRepos[repositoryInterface] = repositoryInstance as Any
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getRepository(conexao: Conexao, repositoryInterface: Class<*>): T? {
        return repositoriesByConnection[conexao]?.get(repositoryInterface) as? T
    }

    fun registerEntityManager(conexao: Conexao, entityManager: jakarta.persistence.EntityManager) {
        entityManagersByConnection[conexao] = entityManager
    }

    fun getEntityManager(conexao: Conexao): jakarta.persistence.EntityManager? {
        return entityManagersByConnection[conexao]
    }
}