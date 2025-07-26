package br.com.fenix.apiintegracao.repository

import br.com.fenix.apiintegracao.model.EntityBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.time.LocalDateTime

@NoRepositoryBean
interface RepositoryJpaBase<E : EntityBase<E, ID>, ID> : JpaRepository<E, ID> {
    fun findAllByAtualizacaoGreaterThanEqual(atualizacao : LocalDateTime, pageable: Pageable) : Page<E>
    fun findAllByAtualizacaoGreaterThanEqual(atualizacao : LocalDateTime) : List<E>
}