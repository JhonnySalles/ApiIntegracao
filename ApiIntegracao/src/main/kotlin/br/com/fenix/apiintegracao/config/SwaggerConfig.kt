package br.com.fenix.apiintegracao.config

import br.com.fenix.apiintegracao.controller.ControllerJdbcBase
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemFull
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseItemSmall
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.ArraySchema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
                    Pair(parameterizedType.actualTypeArguments[0] as Class<*>, parameterizedType.actualTypeArguments[2] as Class<*>)
                }
            )

            val controllerToDtoMapSmall = applicationContext.getBeansOfType(ControllerJdbcBaseItemSmall::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    Pair(parameterizedType.actualTypeArguments[0] as Class<*>, parameterizedType.actualTypeArguments[2] as Class<*>)
                }
            )

            val controllerToDtoMapFull = applicationContext.getBeansOfType(ControllerJdbcBaseItemFull::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    Pair(parameterizedType.actualTypeArguments[0] as Class<*>, parameterizedType.actualTypeArguments[2] as Class<*>)
                }
            )

            val controllerToDtoMap = applicationContext.getBeansOfType(ControllerJdbcBase::class.java).values.associateBy(
                { it.javaClass.name },
                { controller ->
                    val parameterizedType = controller.javaClass.genericSuperclass as ParameterizedType
                    Pair(parameterizedType.actualTypeArguments[0] as Class<*>, parameterizedType.actualTypeArguments[2] as Class<*>)
                }
            )

            openApi.paths.values.forEach { pathItem ->
                pathItem.readOperationsMap().forEach { (httpMethod, operation) ->

                    var concreteDtoClass: Class<*>? = null
                    //Utilize sempre a tag com parte do nome da classe dto para poder ser identificado
                    val opId = operation.operationId.substringBefore("_")
                    when (opId) {
                        // Parâmetro com objeto
                        "updateListBaseTabela", "saveListBaseTabela" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.second
                        }

                        "updateListBaseSmall", "saveListBaseSmall" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapSmall.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.second
                        }

                        "updateListBaseFull", "saveListBaseFull" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapFull.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.second
                        }

                        "deleteListIdBaseTabela" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.first
                        }

                        "deleteListIdBaseSmall" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapSmall.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.first
                        }

                        "deleteListIdBaseFull" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")
                            concreteDtoClass = controllerToDtoMapFull.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.first
                        }

                        "deleteListIdBase" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")

                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.first

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapSmall.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.first

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapFull.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.first

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMap.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.first
                        }

                        // Base raiz
                        "partialListBase", "saveListBase", "updateListBase" -> {
                            val controllerName = (operation.tags?.firstOrNull() ?: "").replace(" ", "").substringBefore("—")

                            concreteDtoClass = controllerToDtoMapTabela.values.find {
                                it.second.simpleName.replace("Dto", "") == controllerName
                            }?.second

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapSmall.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.second

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMapFull.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.second

                            if (concreteDtoClass == null)
                                concreteDtoClass = controllerToDtoMap.values.find {
                                    it.second.simpleName.replace("Dto", "") == controllerName
                                }?.second
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

                    if (httpMethod == PathItem.HttpMethod.POST || httpMethod == PathItem.HttpMethod.PUT || httpMethod == PathItem.HttpMethod.PATCH) {
                        val content = operation.requestBody?.content?.get("application/json")
                        val schema = content?.schema

                        if (schema != null) {
                            if (schema.`$ref` != null && schema.`$ref`.endsWith("_Summary"))
                                schema.`$ref` = schema.`$ref`.replace("_Summary", "")

                            if (schema is ArraySchema && schema.items.`$ref` != null && schema.items.`$ref`.endsWith("_Summary"))
                                schema.items.`$ref` = schema.items.`$ref`.replace("_Summary", "")
                        }
                    }
                }
            }
        }
    }
}