package util

import Resources

private const val PandocExe = "$Resources/converter/pandoc.exe"

object Pandoc {
    fun convert(dokuWikiFile: String): String = getCommandOutput(
            "$PandocExe --from dokuwiki --to gfm $dokuWikiFile".split(" ")
    )
}
