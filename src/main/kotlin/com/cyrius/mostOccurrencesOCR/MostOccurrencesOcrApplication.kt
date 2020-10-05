package com.cyrius.mostOccurrencesOCR

import net.sourceforge.tess4j.Tesseract
import java.io.File

val config = LocalFileConfiguration("./config.json").configuration
val tesseract = tesseractFactory(config.dpi,
		config.languages,
		config.tessDataPath)

fun main() {
	while(true) {
		moveToProcPath()
		processFile()
	}
}

private fun tesseractFactory(dpi: String, languages: String, tessDataPath: String): Tesseract{
	val tesseract = Tesseract()
	tesseract.setDatapath(tessDataPath)
	tesseract.setTessVariable("user_defined_dpi", dpi)
	tesseract.setLanguage(languages)
	return tesseract
}

private fun moveToProcPath() {
	File(config.inPath).listFiles()?.forEach{ file ->
		if(file.isFile) {
			Thread.sleep(2000)
			if(file.lastModified() == File(file.path).lastModified()){
				file.copyTo(File("${config.procPath}${File.separator}${file.name}"), true)
				file.delete()
			}
		}
	}
}

private fun processFile() {
	File(config.procPath).listFiles()?.forEach { file ->
		if(file.isFile) {
			println("File name: $file")
			try {
				val map = tesseract
						.doOCR(file)
						.split(" ")
						.groupingBy { word ->
							config.valueToEliminate.forEach() { toEliminate ->
								word.replace(toEliminate, "")
							}
							word.toLowerCase()
						}
						.eachCount()
				println(map.toList().sortedBy{(_,value) -> value}.toMap())
				val filteredMap = map.filter { (word, value) -> value > config.threshold_count
						&& word.length > config.threshold_length }
				val box = Box(file.nameWithoutExtension, file, filteredMap.keys.toList())
				BoxesAPIClient()
						.auth()
						.createBox(box)
				file.copyTo(File("${config.archPath}${File.separator}${file.name}"), true)
				file.delete()
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}
}