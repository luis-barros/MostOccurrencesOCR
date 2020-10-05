package com.cyrius.mostOccurrencesOCR

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.util.LoadLibs
import java.io.File



fun main(args: Array<String>) {

	val config = LocalFileConfiguration(args[0])

	val tesseract = tesseractFactory(config.configuration.dpi,
			config.configuration.languages,
			config.configuration.tessDataPath)

	while(true) {
		File(config.configuration.inPath).listFiles()?.forEach{ file ->
			if(file.isFile) {
				Thread.sleep(2000)
				if(file.lastModified() == File(file.path).lastModified()){
					file.copyTo(File("${config.configuration.procPath}${File.separator}${file.name}"), true)
					file.delete()
				}
			}
		}
		File(config.configuration.procPath).listFiles()?.forEach { file ->
			if(file.isFile) {
				println("File name: $file")
				try {
					val map = tesseract
							.doOCR(file)
							.split(" ")
							.groupingBy { word ->
								word.toLowerCase()
										.replace(".", "")
										.replace(",", "")}
							.eachCount()
					println(map.toList().sortedBy{(_,value) -> value}.toMap())
					val filteredMap = map.filter { (word, value) -> value > config.configuration.threshold_count
									&& word.length > config.configuration.threshold_length }
					val box = Box(file.nameWithoutExtension, config, file, filteredMap.keys.toList())
					BoxesAPIClient()
							.auth(config)
							.createBox(config, box)
					file.copyTo(File("${config.configuration.archPath}${File.separator}${file.name}"), true)
					file.delete()
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
	}
}

private fun tesseractFactory(dpi: String, languages: String, tessDataPath: String): Tesseract{
	val tesseract = Tesseract()
	tesseract.setDatapath(tessDataPath)
	tesseract.setTessVariable("user_defined_dpi", dpi)
	tesseract.setLanguage(languages)
	return tesseract
}