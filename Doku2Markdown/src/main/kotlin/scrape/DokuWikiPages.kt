package scrape

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
        println("Visiting $url")
        val res = Jsoup.connect(url)
                .data(LoginCookieKey, System.getenv(LoginCookieValue))
                .method(Connection.Method.POST)
                .execute()

        val cookies = res.cookies()
        val doc = Jsoup.connect(url).cookies(cookies).get()
        val text = doc.selectFirst("#wiki__text")?.text()
        if (text != null) {
            File(page.localRawDokuWikiDirectory).mkdirs()
            File(page.localRawDokuWikiPath).writeText(text)
        } else {
            println("Could not find wiki text in page: $doc")
        }


    }
}