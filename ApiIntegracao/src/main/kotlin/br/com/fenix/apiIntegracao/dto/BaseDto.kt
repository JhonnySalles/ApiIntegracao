package br.com.fenix.apiIntegracao.dto

import org.springframework.hateoas.RepresentationModel
import java.io.Serializable

abstract class BaseDto<ID>() : RepresentationModel<BaseDto<ID>>(), Dto<ID>, Serializable {

}