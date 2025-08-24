package br.com.fenix.apiintegracao.config

import br.com.fenix.apiintegracao.dto.mangaextractor.MangaCapaDto
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaCapituloDto
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaVolumeDto
import br.com.fenix.apiintegracao.dto.novelextractor.NovelCapaDto
import br.com.fenix.apiintegracao.dto.novelextractor.NovelCapituloDto
import br.com.fenix.apiintegracao.dto.novelextractor.NovelVolumeDto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapa
import br.com.fenix.apiintegracao.model.mangaextractor.MangaCapitulo
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.model.novelextractor.NovelCapa
import br.com.fenix.apiintegracao.model.novelextractor.NovelCapitulo
import br.com.fenix.apiintegracao.model.novelextractor.NovelVolume
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.modelmapper.convention.MatchingStrategies
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Configuration
class ModelMapperConfig {

    companion object {
        private val oLog = LoggerFactory.getLogger(ModelMapperConfig::class.java.name)
    }

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()

        modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        val convertBase64ToBufferedImage = { base64String: String? ->
            if (base64String.isNullOrBlank() || !base64String.contains(","))
                null
            else {
                try {
                    val base64Data = base64String.substringAfter(',')
                    val imageBytes = Base64.getDecoder().decode(base64Data)
                    ByteArrayInputStream(imageBytes).use { inputStream -> ImageIO.read(inputStream) }
                } catch (e: Exception) {
                    oLog.error("Falha ao converter o base64 em imagem. Erro: ${e.message}", e)
                    null
                }
            }
        }

        val convertBufferedImageToBase64 = { imagem: BufferedImage?, extensao: String? ->
            if (imagem == null || extensao.isNullOrBlank())
                null
            else {
                try {
                    val baos = ByteArrayOutputStream()
                    ImageIO.write(imagem, extensao, baos)
                    val bytes = baos.toByteArray()
                    if (bytes.isEmpty()) {
                        null
                    } else {
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
                } catch (e: Exception) {
                    oLog.error("Falha ao converter a imagem em base64. Erro: ${e.message}", e)
                    null
                }
            }
        }

        // --- Mapeamento para MangaCapa ---
        modelMapper.createTypeMap(MangaCapaDto::class.java, MangaCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToBufferedImage(dto.imagem)
            entidade
        }

        modelMapper.createTypeMap(MangaCapa::class.java, MangaCapaDto::class.java).setPostConverter { context ->
            val entidade = context.source
            val dto = context.destination
            dto.imagem = convertBufferedImageToBase64(entidade.imagem, entidade.extenssao)
            dto
        }

        modelMapper.createTypeMap(MangaVolume::class.java, MangaVolumeDto::class.java).setPostConverter { context ->
            val source = context.source
            val destination = context.destination

            if (source.capa != null)
                destination.capa = modelMapper.map(source.capa, MangaCapaDto::class.java)

            if (source.capitulos != null && source.capitulos.isNotEmpty()) {
                val listType = object : TypeToken<List<MangaCapituloDto>>() {}.type
                destination.capitulos = modelMapper.map(source.capitulos, listType)
            }

            destination
        }

        modelMapper.createTypeMap(MangaVolumeDto::class.java, MangaVolume::class.java).setPostConverter { context ->
            val source = context.source
            val destination = context.destination

            if (source.capa != null)
                destination.capa = modelMapper.map(source.capa, MangaCapa::class.java)

            if (source.capitulos.isNotEmpty()) {
                val listType = object : TypeToken<List<MangaCapitulo>>() {}.type
                destination.capitulos = modelMapper.map(source.capitulos, listType)
            }

            destination
        }

        // --- Mapeamento para NovelCapa ---
        modelMapper.createTypeMap(NovelCapaDto::class.java, NovelCapa::class.java).setPostConverter { context ->
            val dto = context.source
            val entidade = context.destination
            entidade.imagem = convertBase64ToBufferedImage(dto.imagem)
            entidade
        }

        modelMapper.createTypeMap(NovelCapa::class.java, NovelCapaDto::class.java).setPostConverter { context ->
            val entidade = context.source
            val dto = context.destination
            dto.imagem = convertBufferedImageToBase64(entidade.imagem, entidade.extenssao)
            dto
        }

        modelMapper.createTypeMap(NovelVolume::class.java, NovelVolumeDto::class.java).setPostConverter { context ->
            val source = context.source
            val destination = context.destination

            if (source.capa != null)
                destination.capa = modelMapper.map(source.capa, NovelCapaDto::class.java)

            if (source.capitulos != null && source.capitulos.isNotEmpty()) {
                val listType = object : TypeToken<List<NovelCapituloDto>>() {}.type
                destination.capitulos = modelMapper.map(source.capitulos, listType)
            }

            destination
        }

        modelMapper.createTypeMap(NovelVolumeDto::class.java, NovelVolume::class.java).setPostConverter { context ->
            val source = context.source
            val destination = context.destination

            if (source.capa != null)
                destination.capa = modelMapper.map(source.capa, NovelCapa::class.java)

            if (source.capitulos.isNotEmpty()) {
                val listType = object : TypeToken<List<NovelCapitulo>>() {}.type
                destination.capitulos = modelMapper.map(source.capitulos, listType)
            }

            destination
        }

        return modelMapper
    }
}