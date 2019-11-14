package scrape

import it.skrape.selects.element
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File

private const val LoginCookieKey = "DWd6fcb57a725757b22fe830cccebe05e6"
// Feel
private const val LoginCookieValue = "DOKUWIKI_PASSWORD_HASH"

fun scrapeAndSaveDokuWikiPages() {
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