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

        val toBufferedImageConverter = Converter<String?, BufferedImage?> { context ->
            val source = context.source
            if (source.isNullOrBlank() || !source.contains(","))
                return@Converter null

            try {
                val base64Data = source.substringAfter(',')
                val imageBytes = Base64.getDecoder().decode(base64Data)
                ByteArrayInputStream(imageBytes).use { inputStream ->
                    ImageIO.read(inputStream)
                }
            } catch (e: Exception) {
                null
            }
        }

        modelMapper.typeMap(MangaCapaDto::class.java, MangaCapa::class.java).addMappings { mapper ->
            mapper.using(toBufferedImageConverter).map(
                { dto -> dto.imagem },
                { destino, valor : BufferedImage? -> destino.imagem = valor }
            )
        }

        modelMapper.typeMap(NovelCapaDto::class.java, NovelCapa::class.java).addMappings { mapper ->
            mapper.using(toBufferedImageConverter).map(
                { dto -> dto.imagem },
                { destino, valor : BufferedImage? -> destino.imagem = valor }
            )
        }

        return modelMapper
    }
}