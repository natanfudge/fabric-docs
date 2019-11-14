package scrape

import JsonConfig
import Resources
import it.skrape.extract
import it.skrape.selects.element
import it.skrape.selects.elements
import it.skrape.skrape
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.list
import migratePath
import java.io.File

 const val MarkdownDirectory = "$Resources/pages_markdown/"

@Serializable
data class Page(val tag: String?, val name: String) {
    @Transient
    val url = "https://fabricmc.net/wiki/${if (tag == null) "" else "$tag:"}$name"
    @Transient
    val editUrl = "$url?do=edit"

    fun revisionsUrl(first: Int = 0) = "$url?do=revisions&first=$first"

    @Transient
    private val relativeDirectoryPath = tag?.split(":")?.joinToString("/")?.plus("/") ?: ""
    @Transient
    val localDokuWikiDirectory = "$Resources/pages_dokuwiki/$relativeDirectoryPath"
    @Transient
    val localDokuWikiPath = "$localDokuWikiDirectory$name.txt"


    @Transient
    val relativeMarkdownPath = migratePath("$relativeDirectoryPath$name.md")


    @Transient
    val localMarkdownPath = "$MarkdownDirectory$relativeMarkdownPath"

    companion object {
        fun getPages() = JsonConfig.parse(serializer().list, File(Pages).readText()).filter { it.name !in BannedPages }
    }
}


fun notNamespaced(name: String) = Page(null, name)
fun frenchTutorial(name: String) = Page("fr:tutoriel", name)

const val Pages = "$Resources/pages.json"
val BannedPages = listOf("dokuwiki", "syntax", "welcome", "agenda", "wiki_meta","sidebar","start","accueil")


fun scrapeAndSavePageList() {
    val tags = skrape {
        url = "https://fabricmc.net/wiki/start?do=index"

        extract {
            elements(".idx_dir").map { it.text() }
        }
    }

    val notNamespaced = listOf("changelog", "install", "rules", "sidebar_do_edit", "start", "wiki_meta")
    val frenchTutorials = listOf(
            "ajouter_mods", "appliquer_modifications", "blocs", "enchantements",
            "groupes_objets", "infobulles", "installation_java", "mise_en_place", "modpacks_atlauncher", "modpacks_technic",
            "objets", "recettes", "termes"
    )

    val pages = tags.flatMap { tag ->
        skrape {
            url = "https://fabricmc.net/wiki/start?idx=$tag"

            extract {
                element("#index__tree .open .idx").children().map { Page(tag = tag, name = it.text()) }
            }
        }
    }.filter {
        it != Page(
                "fr",
                "tutoriel"
        )
    } + notNamespaced.map { notNamespaced(it) } + frenchTutorials.map { frenchTutorial(it) }



    File(Pages).writeText(
            JsonConfig.stringify(Page.serializer().list, pages.filter { it.name !in BannedPages })
    )
}