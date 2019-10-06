import it.skrape.extract
import it.skrape.selects.elements
import it.skrape.skrape
import kotlinx.serialization.Serializable
import kotlinx.serialization.list
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private const val AuthorsPath = "$Resources/authors.json"

private const val AuthorsLinksPath = "$Resources/authorLinks.properties"


fun scrapeAuthors(page: Page): Set<String> {
    val authors = mutableSetOf<String>()
    var first = 0
    do {
        var nextPageExists = false
        val authorsColumn = skrape {
            url = page.revisionsUrl(first)

            extract {
                val lessRecentButton = elements(".no button").filter { it.attr("title") == "less recent >> [N]" }
                if (lessRecentButton.isNotEmpty()) nextPageExists = true
                elements("#page__revisions .user").map { it.text() }.toSet() + authors
            }
        }

        authors.addAll(authorsColumn)
        first += 20
    } while (nextPageExists)

    return authors
}

@Serializable
data class PageAuthors(val page: Page, val authors: Set<String>)

fun scrapeAndWriteAuthors() {
    val authors = Page.getPages().map { PageAuthors(it, scrapeAuthors(it)) }
    File(AuthorsPath).writeText(JsonConfig.stringify(PageAuthors.serializer().list, authors))
}

fun writeAllAuthors() {
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

const val CreditsFilePath = "docs/CREDITS.md"

fun writeCreditsFile() {
    val introduction = """
        Most of the content provided here (at the time of writing) is imported from [the DokuWiki Fabric Wiki](https://fabricmc.net/wiki/doku.php)
         and licensed under the [CC Attribution-Noncommercial-Share Alike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/) license.
        This document details the authors of each page. 
        Each page was originally written in the DokuWiki format, and have been converted to Markdown.
        Additionally, changes were made on pages that had additional contributions since they were imported, as detailed in [the git history](https://github.com/natanfudge/fabric-docs/commits/master).
        The pages listed are in relation to the `/docs` directory of [the Github repository](https://github.com/natanfudge/fabric-docs).
        
        
    """.trimIndent()

    val authorLinks = Properties()
    Files.newInputStream(Paths.get(AuthorsLinksPath)).use {
        authorLinks.load(it)
    }

    val authors = JsonConfig.parse(PageAuthors.serializer().list, File(AuthorsPath).readText()).joinToString("\n\n") { pageAuthors ->
        """
Page: ${pageAuthors.page.relativeMarkdownPath}  
Authors:
${pageAuthors.authors.joinToString("\n") {
            val str = if (authorLinks.containsKey(it)) "[$it](${authorLinks.getProperty(it)})"
            else it
            
            " - $str  "
        }}
Original Page: ${pageAuthors.page.url}  
    """.trimIndent()
    }

    File(CreditsFilePath).writeText(introduction +  authors)
}
