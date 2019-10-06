import it.skrape.extract
import it.skrape.selects.element
import it.skrape.selects.elements
import it.skrape.skrape
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.set
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


//pandoc --from dokuwiki --to gfm

const val LoginCookieKey = "DWd6fcb57a725757b22fe830cccebe05e6"
// Feel
const val LoginCookieValue = "DOKUWIKI_PASSWORD_HASH"

const val ImageListPath = "$Resources/images.json"


val JsonConfig = Json(JsonConfiguration.Stable.copy(prettyPrint = true))


fun scrapeAndWriteDokuWikiPages() {
    for (page in Json(JsonConfiguration.Stable).parse(
            Page.serializer().list,
            File(Pages).readText()
    )) {
        val url = page.editUrl
        val res = Jsoup.connect(url)
                .data(LoginCookieKey, System.getenv(LoginCookieValue))
                .method(Connection.Method.POST)
                .execute()

        val cookies = res.cookies()
        val doc = Jsoup.connect(url).cookies(cookies).get()
        println(doc)
        val text = doc.element("#wiki__text").text()


        File(page.localDokuWikiDirectory).mkdirs()
        File(page.localDokuWikiPath).writeText(text)
    }
}

fun scrapeAndWriteImageList() {
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

fun downloadAndWriteImages() {
    val images = Json(JsonConfiguration.Stable).parse(StringSerializer.list, File(ImageListPath).readText())
            .filter { it.startsWith("/wiki/_media") }

    for (image in images) {
        val relativePath = image.removePrefix("/wiki/_media").split("?")[0].replace(":", "/")
        val localPath = "$Resources/pages_markdown/images/$relativePath"
        File(localPath).parentFile.mkdirs()
        val httpsPath = URL("https://fabricmc.net$image")
        httpsPath.openStream().use {
            Files.copy(it, Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING)
        }
    }

}



fun main() {
    convertToMarkdown()
    writeCreditsFile()
}