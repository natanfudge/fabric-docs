package util

import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkNodeBase
import com.vladsch.flexmark.ast.Reference
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import com.vladsch.flexmark.util.ast.Visitor
import com.vladsch.flexmark.util.sequence.SegmentedSequence
import migratePath

private inline fun <reified T : Node> visitHandler(crossinline visitor: (T) -> Unit) = VisitHandler(
        T::class.java,
        Visitor<T> { node -> visitor(node) }
)

fun Node.fixLinks(nestingLevel: Int) {
    NodeVisitor(
            visitHandler<Link>{ visit(it, nestingLevel) },
            visitHandler<Reference> { visit(it,nestingLevel) },
            visitHandler<Image> { visit(it,nestingLevel,image = true) }
    ).visit(this)
}


private fun visit(node: LinkNodeBase, nestingLevel: Int, image: Boolean = false) {
    if (!node.pageRef.startsWith("http")) {
        val linkBase = node.pageRef.toString()

        val newRef = if (image) "../".repeat(nestingLevel) + "images" + linkBase
        else "../".repeat(nestingLevel) + migratePath(linkBase.removePrefix("/") + ".md")

        node.pageRef = SegmentedSequence.of(newRef)

    }

}
