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
                    val imageBytes = Base64.getDecoder().decode(base64Data)
                    ByteArrayInputStream(imageBytes).use { inputStream -> ImageIO.read(inputStream) }
                } catch (e: Exception) {
                    null
                }
            }
        }

        // --- Mapeamento para MangaCapa ---
        modelMapper.createTypeMap(MangaCapaDto::class.java, MangaCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToImage(dto.imagem)

            entidade
        }

        modelMapper.createTypeMap(NovelCapaDto::class.java, NovelCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToImage(dto.imagem)
            entidade
        }

        return modelMapper
    }
}