package br.com.fenix.apiintegracao.config

import br.com.fenix.apiintegracao.dto.mangaextractor.MangaCapaDto
import br.com.fenix.apiintegracao.dto.novelextractor.NovelCapaDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapa
import br.com.fenix.apiintegracao.model.novelextractor.NovelCapa
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.util.*
import javax.imageio.ImageIO

@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()

        modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        val convertBase64ToImage = { base64String: String? ->
            if (base64String.isNullOrBlank() || !base64String.contains(","))
                null
            else {
                try {
                    val base64Data = base64String.substringAfter(',')
                    Base64.getDecoder().decode(base64Data)
                } catch (e: Exception) {
                    null
                }
            }
        }

        val convertByteArrayToBase64 = { bytes: ByteArray?, extensao: String? ->
            if (bytes == null || bytes.isEmpty() || extensao.isNullOrBlank())
                null
            else {
                val mimeType = when (extensao.lowercase()) {
                    "png" -> "image/png"
                    "jpg", "jpeg" -> "image/jpeg"
                    "gif" -> "image/gif"
                    "webp" -> "image/webp"
                    "svg" -> "image/svg+xml"
                    else -> "application/octet-stream"
                }
                val base64String = Base64.getEncoder().encodeToString(bytes)
                "data:$mimeType;base64,$base64String"
            }
        }

        // --- Mapeamento para MangaCapa ---
        modelMapper.createTypeMap(MangaCapaDto::class.java, MangaCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToImage(dto.imagem)
            entidade
        }

        modelMapper.createTypeMap(MangaCapa::class.java, MangaCapaDto::class.java).setPostConverter { context ->
            val entidade = context.source
            val dto = context.destination
            dto.imagem = convertByteArrayToBase64(entidade.imagem, entidade.extenssao)
            dto
        }

        // --- Mapeamento para NovelCapa ---
        modelMapper.createTypeMap(NovelCapaDto::class.java, NovelCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToImage(dto.imagem)
            entidade
        }

        modelMapper.createTypeMap(NovelCapa::class.java, NovelCapaDto::class.java).setPostConverter { context ->
            val entidade = context.source
            val dto = context.destination
            dto.imagem = convertByteArrayToBase64(entidade.imagem, entidade.extenssao)
            dto
        }

        return modelMapper
    }
}