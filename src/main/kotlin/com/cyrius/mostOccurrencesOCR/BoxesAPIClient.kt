package com.cyrius.mostOccurrencesOCR

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel


data class AuthBody(val username: String, val password: String)
data class AuthResp(val token: String, val expire: String)
class BoxesAPIClient {
    private val mapper = jacksonObjectMapper()
    private var token = ""

    fun auth(config: LocalFileConfiguration): BoxesAPIClient {
        val body = AuthBody(config.configuration.user, config.configuration.pwd)
        val data = mapper.writeValueAsString(body)
        val (_, _, result) =
                Fuel.post("${config.configuration.host}:${config.configuration.port}/auth/login")
                .header(mapOf("Content-Type" to "application/json"))
                .body(data)
                .responseString()
        val(payload, _) = result
        if(payload != null) {
            val authResp: AuthResp = mapper.readValue(payload)
            token = authResp.token
        }
        return this
    }

    fun createBox(config: LocalFileConfiguration, payload: Box): BoxesAPIClient {
        val (request, response, result) =
                Fuel.post("${config.configuration.host}:${config.configuration.port}/boxes")
                        .header(mapOf("Content-Type" to "application/json", "Authorization" to "Bearer $token"))
                        .body(payload.serializeMap())
                        .responseString()
        return this
    }
}