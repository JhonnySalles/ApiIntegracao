package br.com.fenix.apiintegracao.utils

import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {
    companion object {
        fun defaultPageable(): Pageable = Pageable.ofSize(10).withPage(0)

        private val dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        fun convertToDateTime(str: String): LocalDateTime = LocalDateTime.parse(str, dateTime)
        fun convertToString(ldt: LocalDateTime): String = ldt.format(dateTime)

        private val date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        fun convertToString(ldt: LocalDate): String = ldt.format(date)
    }
}