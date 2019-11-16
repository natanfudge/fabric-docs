package produce

import scrape.DocsDirectory
import scrape.MarkdownDirectory
import scrape.RootDirectory
import util.copyContentsRecursively
import java.io.File

const val NewDocsDirectory = RootDirectory + "newdocs/"
fun combineNewAndOldDocs(includeFrench: Boolean) {
    File(MarkdownDirectory).copyContentsRecursively(File(DocsDirectory)) {file ->
        file.path.split(File.separator).any { it == "French" } == includeFrench
    }
//    for (page in Page.getPages().filter { it.isFrench == includeFrench }) {
//        File(page.localMarkdownPath).copyTo(File(page.localExposedPath), overwrite = true)
//    }
    File(NewDocsDirectory).copyContentsRecursively(File(DocsDirectory))
}

fun exposeDocs(includeFrench: Boolean) {
    combineNewAndOldDocs(includeFrench)
    produceCredits()
}