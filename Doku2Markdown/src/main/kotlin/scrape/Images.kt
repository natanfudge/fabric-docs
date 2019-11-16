package scrape

import Resources
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

const val ImagesPath = "$MarkdownDirectory/images/"
fun downloadAndSaveImages() {
    val images = Json(JsonConfiguration.Stable).parse(StringSerializer.list, File(ImageListPath).readText())
            .filter { it.startsWith("/wiki/_media") }

    for (image in images) {
        val relativePath = image.removePrefix("/wiki/_media").split("?")[0].replace(":", "/")
        val localPath = "$ImagesPath$relativePath"
        File(localPath).parentFile.mkdirs()
        val httpsPath = URL("https://fabricmc.net$image")
        println("Downloading from $httpsPath")
        httpsPath.openStream().use {
            Files.copy(it, Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING)
        }
    }

}