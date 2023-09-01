package br.com.fenix.apiIntegracao.dto

import org.springframework.hateoas.RepresentationModel
import java.io.Serializable

abstract class DtoBase<ID>() : RepresentationModel<DtoBase<ID>>(), Dto<ID>, Serializable {

}