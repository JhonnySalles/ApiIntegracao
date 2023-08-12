package br.com.fenix.apiIntegracao.converters

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter


class YamlJackson2HttpMesageConverter : AbstractJackson2HttpMessageConverter(
    YAMLMapper()
        .setSerializationInclusion(
            JsonInclude.Include.NON_NULL
        ),
    MediaType.parseMediaType("application/x-yaml")
)