package br.com.fenix.apiIntegracao.controller.withyaml.mapper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.restassured.mapper.ObjectMapper
import io.restassured.mapper.ObjectMapperDeserializationContext
import io.restassured.mapper.ObjectMapperSerializationContext
import java.util.logging.Logger

class YMLMapper : ObjectMapper {

    private val logger = Logger.getLogger(YMLMapper::class.java.name)

    private var objectMapper: com.fasterxml.jackson.databind.ObjectMapper = com.fasterxml.jackson.databind.ObjectMapper(YAMLFactory())
    protected var typeFactory: TypeFactory

    init {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        typeFactory = TypeFactory.defaultInstance()
    }

    override fun deserialize(context : ObjectMapperDeserializationContext): Any? {
        try {
            val dataToDeserialize: String = context.getDataToDeserialize().asString()
            val type = context.getType() as Class<*>
            logger.info("Trying deserialize object of type$type")
            return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type))
        } catch (e: JsonMappingException) {
            logger.severe("Error deserializing object")
            e.printStackTrace()
        } catch (e: JsonProcessingException) {
            logger.severe("Error deserializing object")
            e.printStackTrace()
        }
        return null
    }

    override fun serialize(context : ObjectMapperSerializationContext): Any? {
        try {
            return objectMapper.writeValueAsString(context.objectToSerialize)
        } catch (e: JsonProcessingException) {
            logger.severe("Error deserializing object")
        }
        return null
    }
}