package scrape

fun scrapeAndSaveEverything() {
    scrapeAndSaveAuthors()
    scrapeAndSavePageList()
    scrapeAndSaveDokuWikiPages()
    scrapeAndSaveImageList()
    downloadAndSaveImages()
}