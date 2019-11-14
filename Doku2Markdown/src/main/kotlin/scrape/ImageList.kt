package scrape

import Resources
import it.skrape.extract
import it.skrape.selects.elements
import it.skrape.skrape
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.set
import java.io.File

const val ImageListPath = "$Resources/images.json"

fun scrapeAndSaveImageList() {
    val images = mutableSetOf<String>()
    for (page in Page.getPages()) {
        val pageImages = skrape {
            url = page.url

            extract {
                elements("#dokuwiki__content img").map { it.attr("src") }
            }
        }
        images.addAll(pageImages)
    }

    File(ImageListPath).writeText(
            Json(JsonConfiguration.Stable.copy(prettyPrint = true)).stringify(
                    StringSerializer.set,
                    images
            )
    )
}