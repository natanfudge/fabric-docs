import org.junit.Test
import produce.*
import scrape.*
import java.io.File

private fun cleanMarkdown(dir: File = File(MarkdownDirectory)) {
    if (!dir.exists()) dir.mkdirs()
    for (file in dir.listFiles()!!) {
        if (file.name != "images") {
            file.deleteRecursively()
        }
    }
}

//private fun cleanDocs(dir: File = File(MarkdownDirectory)) {
//    if(!dir.exists()) dir.mkdirs()
//    for (file in dir.listFiles()!!) {
//        if (file.name != "images") {
//            file.deleteRecursively()
//        }
//    }
//}


class ControlRoom {

    @Test
    fun updateEnglishPages() {
        updatePages()
        refreshEnglishDocs()
    }

    @Test
    fun updateFrenchPages() {
        updatePages()
        refreshFrenchDocs()
    }

    @Test
    fun cleanRun() {
        scrapeAndSaveEverything()
        produceEverything()
    }

    @Test
    fun updateAllLocal() {
        reconvertMarkdownPages()
        refreshCredits()
        refreshEnglishDocs()
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
    fun refreshEnglishDocs() {
        File(DocsDirectory).deleteRecursively()
        exposeDocs(includeFrench = false)
    }

    @Test
    fun refreshFrenchDocs() {
        File(DocsDirectory).deleteRecursively()
        exposeDocs(includeFrench = true)
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

    @Test
    fun redownloadImages() {
        downloadAndSaveImages()
    }

    private fun updatePages() {
        scrapeAndSavePageList()
        scrapeAndSaveDokuWikiPages()
        reconvertMarkdownPages()
    }
}