package produce

import JsonConfig
import Resources
import kotlinx.serialization.list
import scrape.AuthorsPath
import scrape.PageAuthors
import java.io.File
import java.io.FileWriter
import java.util.*

fun produceAuthorsList() {
    val authors = JsonConfig.parse(PageAuthors.serializer().list, File(AuthorsPath).readText())
    val allAuthors = authors.flatMap { it.authors }.toSet()
    val properties = Properties()
    for (author in allAuthors) {
        properties[author] = ""
    }

    FileWriter("$Resources/allAuthors.properties").use {
        properties.store(it, "allAuthors")
    }

}