package br.com.fenix.apiintegracao.config

import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemFull
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemSmall
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.media.ArraySchema
import org.springdoc.core.customizers.OpenApiCustomizer
import java.lang.reflect.ParameterizedType

@Configuration
class SwaggerConfig(private val applicationContext: ApplicationContext) {
    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI().info(
            Info().title("Api Integração").version("v1").description("").termsOfService("").license(
                License().name("Apache 2.0").url("")
            )
        )
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            val controllerToDtoMapTabela = applicationContext.getBeansOfType(ControllerJdbcBaseTabela::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    parameterizedType.actualTypeArguments[2] as Class<*>
                }
            )

            val controllerToDtoMapSmall = applicationContext.getBeansOfType(ControllerJdbcBaseItemSmall::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    parameterizedType.actualTypeArguments[2] as Class<*>
                }
            )

            val controllerToDtoMapFull = applicationContext.getBeansOfType(ControllerJdbcBaseItemFull::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    parameterizedType.actualTypeArguments[2] as Class<*>
                }
            )

            openApi.paths.values.forEach { pathItem ->
                pathItem.readOperationsMap().forEach { (httpMethod, operation) ->
                    var concreteDtoClass : Class<*>? = null
                    //Utilize sempre a tag com parte do nome da classe dto para poder ser identificado
                    val opId = operation.operationId.substringBefore("_")
                    when (opId) {
                        // Parâmetro com objeto
                        "updateListBaseTabela", "saveListBaseTabela" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.simpleName.replace("Dto", "") == controllerName
                            }
                        }
                        "updateListBaseSmall", "saveListBaseSmall" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapSmall.values.find {
                                it.simpleName.replace("Dto", "") == controllerName
                            }
                        }
                        "updateListBaseFull", "saveListBaseFull" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapFull.values.find {
                                it.simpleName.replace("Dto", "") == controllerName
                            }
                        }

                        // Base raiz
                        "partialListBase", "saveListBase", "updateListBase" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")

                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.simpleName.replace("Dto", "") == controllerName
                            }

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapSmall.values.find {
                                    it.simpleName.replace("Dto", "") == controllerName
                                }

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapFull.values.find {
                                    it.simpleName.replace("Dto", "") == controllerName
                                }
                        }

                    }

                    if (concreteDtoClass != null) {
                        val requestBody = operation.requestBody
                        val content = requestBody.content["application/json"]

                        if (content?.schema is ArraySchema) {
                            val arraySchema = content.schema as ArraySchema
                            arraySchema.items.`$ref` = "#/components/schemas/${concreteDtoClass.simpleName}"
                        }
                    }
                }
            }
        }
    }
}