package br.com.fenix.apiintegracao.validations

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [Base64Validator::class])
@MustBeDocumented
annotation class IsBase64(
    val message: String = "String não é um Base64 válido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)