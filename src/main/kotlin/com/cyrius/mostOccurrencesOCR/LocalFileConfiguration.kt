package com.cyrius.mostOccurrencesOCR

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


class LocalFileConfiguration(val configPath: String) {
    private val mapper = jacksonObjectMapper()
    val configuration: ConfigFile = mapper.readValue(File(configPath))
}
