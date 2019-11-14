import org.junit.Test
import produce.produceAuthorsList
import produce.produceCredits
import produce.produceEverything
import produce.produceMarkdownPages
import scrape.*
import java.io.File

private fun cleanMarkdown(dir: File = File(MarkdownDirectory)) {
    for (file in dir.listFiles()!!) {
        if (file.name != "images") {
            file.deleteRecursively()
        }
    }
}

class ControlRoom {
    @Test
    fun cleanRun() {
        scrapeAndSaveEverything()
        produceEverything()
    }

    @Test
    fun refreshCredits() {
        produceCredits()
    }

    @Test
    fun reconvertMarkdownPages() {
        cleanMarkdown()
        produceMarkdownPages()
    }

    @Test
    fun updatePages() {
        scrapeAndSavePageList()
        scrapeAndSaveDokuWikiPages()
    }

    @Test
    fun redownloadImages() {
        downloadAndSaveImages()
    }

    @Test
    fun updateImages() {
        scrapeAndSaveImageList()
        downloadAndSaveImages()
    }

    @Test
    fun recreateAuthorsList() {
        scrapeAndSaveAuthors()
        produceAuthorsList()
    }

}