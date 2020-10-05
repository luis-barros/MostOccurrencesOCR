package com.cyrius.mostOccurrencesOCR

data class ConfigFile(val inPath: String, val archPath: String, val host: String, val port: String,
    val group: String, val assetsPath: String, val assetsURL: String, val user: String,
    val pwd: String, val procPath: String, val dpi: String, val languages: String, val threshold_length: Int,
val threshold_count: Int, val tessDataPath: String, val valueToEliminate: List<String> )