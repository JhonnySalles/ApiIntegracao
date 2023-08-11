package br.com.fenix.apiIntegracao.exceptions

import java.io.Serializable
import java.util.*

class ExceptionResponse(val timestamp: Date, message: String?, details: String) : Serializable {
}