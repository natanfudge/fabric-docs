package produce

import com.vladsch.flexmark.formatter.Formatter
import com.vladsch.flexmark.parser.Parser
import scrape.Page
import util.Pandoc
import util.fixLinks
import java.io.File

fun produceMarkdownPages() {
    fixDokuWikiPages()
    val lineBreakRegex = Regex("(\\[.*)\\r\\n (.*\\])")
    for (page in Page.getPages()) {
        val markdown = Pandoc.convert(page.localFixedDokuWikiPath)

        val parsed = Parser.builder().build().parse(markdown)
        parsed.fixLinks(nestingLevel = when (page.tag) {
            null -> 0
            else -> page.tag.count { it == ':' } + 1
        })
        val linksFixed = Formatter.builder().build().render(parsed)
        val lineBreaksFixed = lineBreakRegex.replace(linksFixed, "$1$2")

        File(page.localMarkdownPath).apply {
            parentFile.mkdirs()
            writeText(lineBreaksFixed)
        }

    }
}

val indentRegex = Regex("(\\n) {3}(\\*)")
fun fixDokuWikiPages() {
    for (page in Page.getPages()) {
        val raw = File(page.localRawDokuWikiPath).readText()
        val fixed = indentRegex.replace(raw, "$1$2")
        with(File(page.localFixedDokuWikiPath)) {
            parentFile.mkdirs()
            writeText(fixed)
        }
    }
}