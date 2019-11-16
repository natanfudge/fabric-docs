package produce

fun produceEverything() {
    produceAuthorsList()
    produceCredits()
    produceMarkdownPages()
    exposeDocs(includeFrench = false)
//    exposeDocs(includeFrench = true)
}