package com.cyrius.mostOccurrencesOCR

class TextAnalyzer {
    fun analyzeText(t: String) : Map<String, Int> {
        return t.split(" ").groupingBy { word -> word}.eachCount()
    }
}