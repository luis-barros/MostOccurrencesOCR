package com.cyrius.mostOccurrencesOCR

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.util.LoadLibs
import java.io.File

const val THRESHOLD: Int = 5

fun main(args: Array<String>) {
	val tesseract = Tesseract()
	val tessDataFolder : File
			= LoadLibs.extractTessResources("tessdata")
	tesseract.setDatapath(tessDataFolder.absolutePath)
	val config = LocalFileConfiguration(args[0])

	while(true) {
		File(config.configuration.path).listFiles().forEach { file ->
			if(file.isFile) {
				println("File name: $file")
				try {
					val map = tesseract
							.doOCR(file)
							.split(" ")
							.groupingBy { word -> word }
							.eachCount()
							.filter { (_, value) -> value > THRESHOLD }
					val box = Box("Test", config, file, map.keys.toList())
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
