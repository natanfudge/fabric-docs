package produce

import JsonConfig
import Resources
import kotlinx.serialization.list
import scrape.AuthorsPath
import scrape.PageAuthors
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private const val AuthorsLinksPath = "$Resources/authorLinks.properties"
private const val CreditsFilePath = "docs/Credits.md"

fun produceCredits() {
    val introduction = """
        Most of the content provided here (at the time of writing) is imported from [the DokuWiki Fabric Wiki](https://fabricmc.net/wiki/doku.php)
         and licensed under the [CC Attribution-Noncommercial-Share Alike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/) license.
        This document details the authors of each page. 
        Each page was originally written in the DokuWiki format, and has been converted to Markdown.
        Additionally, changes were made on pages that had additional contributions since they were imported, as detailed in [the git history](https://github.com/natanfudge/fabric-docs/commits/master).
        The pages listed are in relation to the `/docs` directory of [the Github repository](https://github.com/natanfudge/fabric-docs).
        
        
    """.trimIndent()

    val authorLinks = Properties()
    Files.newInputStream(Paths.get(AuthorsLinksPath)).use {
        authorLinks.load(it)
    }


    val authors = JsonConfig.parse(PageAuthors.serializer().list, File(AuthorsPath).readText()).joinToString("\n\n") { pageAuthors ->
        """
Page: [${pageAuthors.page.relativeMarkdownPath}](${pageAuthors.page.relativeMarkdownPath})  
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
