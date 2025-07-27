package br.com.fenix.apiintegracao

import br.com.fenix.apiintegracao.dto.AccountCredentialDto

class TestConfigs {
    companion object {
        const val SERVER_PORT = 8888

        const val HEADER_PARAM_AUTHORIZATION = "Authorization"
        const val HEADER_PARAM_ORIGIN = "Origin"

        const val CONTENT_TYPE_JSON = "application/json"
        const val CONTENT_TYPE_XML = "application/xml"
        const val CONTENT_TYPE_YML = "application/x-yaml"

        const val ORIGIN_SEMERU = "https://anotherlink.com.br"
        const val ORIGIN_LOCALHOST = "https://localhost:8080"

        const val ENDPOINT_LOGIN = "/api/auth"

        fun getCredential() : AccountCredentialDto = AccountCredentialDto("jhonny", "jhonny")
    }
}