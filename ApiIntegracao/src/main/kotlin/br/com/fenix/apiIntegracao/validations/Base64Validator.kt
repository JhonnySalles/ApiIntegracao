package br.com.fenix.apiintegracao.validations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.*

class Base64Validator : ConstraintValidator<IsBase64, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null)
            return true

        return try {
            Base64.getDecoder().decode(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}