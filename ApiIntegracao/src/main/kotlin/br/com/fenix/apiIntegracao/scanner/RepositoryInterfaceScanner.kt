package br.com.fenix.apiintegracao.scanner

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider

class RepositoryInterfaceScanner(useDefaultFilters: Boolean) : ClassPathScanningCandidateComponentProvider(useDefaultFilters) {

    override fun isCandidateComponent(beanDefinition: AnnotatedBeanDefinition): Boolean {
        return beanDefinition.metadata.isInterface
    }
}