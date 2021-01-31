import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Test
import produce.produceAuthorsList
import produce.produceCredits
import produce.produceEverything
import produce.produceMarkdownPages
import scrape.*
import java.io.File


const val Resources = "src/main/resources"


val JsonConfig = Json(JsonConfiguration.Stable.copy(prettyPrint = true))


//TODO: automatically merge new docs with old docs
//TODO: automatically fix formatting problems in old docs to minimize the amount of new docs
// (right now it will overwrite any new changes)

fun main() {

}

