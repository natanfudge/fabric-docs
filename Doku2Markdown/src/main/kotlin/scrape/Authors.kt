package scrape

import JsonConfig
import Resources
import it.skrape.extract
import it.skrape.selects.elements
import it.skrape.skrape
import kotlinx.serialization.Serializable
import kotlinx.serialization.list
import java.io.File

 const val AuthorsPath = "$Resources/authors.json"



@Serializable
data class PageAuthors(val page: Page, val authors: Set<String>)

fun scrapeAndSaveAuthors() {
    val authors = Page.getPages().map { PageAuthors(it, scrapeAuthors(it)) }
    File(AuthorsPath).writeText(JsonConfig.stringify(PageAuthors.serializer().list, authors))
}

private const val ChangesPerPage = 20

private fun scrapeAuthors(page: Page): Set<String> {
    val authors = mutableSetOf<String>()
    var firstChangeNumber = 0
    do {
        var nextPageExists = false
        val authorsColumn = skrape {
            url = page.revisionsUrl(firstChangeNumber)

            extract {
                val lessRecentButton = elements(".no button").filter { it.attr("title") == "less recent >> [N]" }
                if (lessRecentButton.isNotEmpty()) nextPageExists = true
                elements("#page__revisions .user").map { it.text() }.toSet() + authors
            }
        }

        authors.addAll(authorsColumn)
        firstChangeNumber += ChangesPerPage
    } while (nextPageExists)

    return authors
}

