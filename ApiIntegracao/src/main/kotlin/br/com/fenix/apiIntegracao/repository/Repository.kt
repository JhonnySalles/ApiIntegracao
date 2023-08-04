package br.com.fenix.apiIntegracao.repository

import br.com.fenix.apiIntegracao.model.Entity
import org.springframework.data.jpa.repository.JpaRepository

import org.springframework.data.repository.NoRepositoryBean


@NoRepositoryBean
interface Repository<T : Entity<T, ID>, ID> : JpaRepository<T, ID>