package com.cyrius.mostOccurrencesOCR

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File



class Box(val name: String, val file: File, val tags: List<String>){
    private val groups: List<String> = mutableListOf<String>(config.group)
    val drops: List<Any> = calcDrops()
    fun serializeMap() : String{
        val map: HashMap<String, Any> = HashMap()
        map["name"] = name
        map["groups"] = groups
        map["drops"] = drops

        val mapper = ObjectMapper()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)
    }

    private fun calcDrops(): List<Any> {
        val imgDrop : java.util.HashMap<String, Any> = java.util.HashMap()
        imgDrop["room"] = "group"
        val data: java.util.HashMap<String, Any> = java.util.HashMap()
        data["type"] = "ORIGINALFILE"
        data["value"] = "${config.assetsURL}/${file.name}"
        imgDrop["data"] = data

        val docDrop: java.util.HashMap<String, Any> = java.util.HashMap()
        val docData: java.util.HashMap<String, Any> = java.util.HashMap()
        docDrop["room"] = "group"
        docData["type"] = "ID_DOC"
        docData["value"] = file.name
        docDrop["data"] = docData

        val tagDrop: java.util.HashMap<String, Any> = java.util.HashMap()
        val tagData: java.util.HashMap<String, Any> = java.util.HashMap()
        // tagDrop["type"] = "room"
        tagDrop["room"] = "group"
        tagData["type"] = "TAGS"
        tagData["value"] = tags
        tagDrop["data"] = tagData

        return mutableListOf<Any>(imgDrop, docDrop, tagDrop)
    }

}



