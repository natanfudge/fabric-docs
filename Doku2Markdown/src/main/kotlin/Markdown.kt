import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkNodeBase
import com.vladsch.flexmark.ast.Reference
import com.vladsch.flexmark.formatter.Formatter
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import com.vladsch.flexmark.util.ast.Visitor
import com.vladsch.flexmark.util.sequence.SegmentedSequence
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

fun convertToMarkdown() {
    for (page in Json(JsonConfiguration.Stable).parse(
        Page.serializer().list,
        File(Pages).readText()
    )) {
        val markdown = runCommandForOutput(
            "converter/pandoc.exe --from dokuwiki --to gfm ${page.localDokuWikiPath}".split(" ")
        )

        val parsed = Parser.builder().build().parse(markdown)
        parsed.fixLinks(nestingLevel = when {
            page.tag == null -> 0
            else -> page.tag.count { it == ':' } + 1
        })
        val fixed = Formatter.builder().build().render(parsed)

        File(page.localMarkdownDirectory).mkdirs()
        File(page.localMarkdownPath).writeText(fixed)

    }
}

fun runCommandForOutput(params: List<String>): String {
    val pb = ProcessBuilder(params)
    val p: Process
    var result = ""
    try {
        p = pb.start()
        val reader = BufferedReader(InputStreamReader(p.inputStream))

        val sj = StringJoiner(System.getProperty("line.separator"))
        reader.lines().iterator().forEachRemaining { sj.add(it) }
        result = sj.toString()

        p.waitFor()
        p.destroy()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun Node.fixLinks(nestingLevel: Int) {
    NodeVisitor(
        VisitHandler<Link>(Link::class.java,
            Visitor<Link> { visit(it, nestingLevel) }),
        VisitHandler<Reference>(
            Reference::class.java,
            Visitor<Reference> { visit(it, nestingLevel) }),
        VisitHandler<Image>(
            Image::class.java,
            Visitor<Image> { visit(it, nestingLevel, image = true) }
        )
    ).visit(this)
}


private fun visit(node: LinkNodeBase, nestingLevel: Int, image: Boolean = false) {
    if (!node.pageRef.startsWith("http")) {
        node.pageRef = SegmentedSequence.of(
            if (image) "../".repeat(nestingLevel) + "images" + node.pageRef.toString()
            else "../".repeat(nestingLevel) + node.pageRef.toString().removePrefix("/") + ".md"
        )
    }
}

