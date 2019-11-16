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

val indentRegex1 = Regex("\\n {3}\\*")
val indentRegex2 = Regex("\\n {6}\\*")
val indentRegex3 = Regex("\\n {9}\\*")

val newLineCodeRegex = Regex("([^\\s].*)(<code.*)")

fun fixDokuWikiPages() {
    for (page in Page.getPages()) {
        val raw = File(page.localRawDokuWikiPath).readText()
        // Markdown doesn't like 3-space indented lines, it turns them into code blocks
        val fixed3spaces = indentRegex1.replace(raw, "\n  *")
        val fixed6spaces = indentRegex2.replace(fixed3spaces, "\n    *")
        val fixed9spaces = indentRegex3.replace(fixed6spaces, "\n      *")

        // Markdown can't start multiline code on the same line
        val fixedSameLineCode = newLineCodeRegex.replace(fixed9spaces,"\$1\n\$2")

        with(File(page.localFixedDokuWikiPath)) {
            parentFile.mkdirs()
            writeText(fixedSameLineCode)
        }
    }
}