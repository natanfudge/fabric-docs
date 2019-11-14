import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


private const val TagMigrationsPath = "$Resources/tagMigrations.properties"
private const val FileMigrationsPath = "$Resources/fileMigrations.properties"
private val tagMigrations = Properties().apply {
    Files.newInputStream(Paths.get(TagMigrationsPath)).use {
        load(it)
    }
}

private val fileMigrations = Properties().apply {
    Files.newInputStream(Paths.get(FileMigrationsPath)).use {
        load(it)
    }
}


fun migratePath(path: String): String {
    if (path.startsWith("http")) return path
    val (pathNoExt, extension) = path.split(".")
    val migratedPath = fileMigrations.getProperty(pathNoExt, pathNoExt)

    val split = migratedPath.split("/")
    val fileName = split.last()
    val directory = split.take(split.size - 1).joinToString("/")
    val migratedDirectory = tagMigrations.getProperty(directory, directory)


    val slashBeforeFile = if (directory.isEmpty()) "" else "/"

    return "$migratedDirectory$slashBeforeFile$fileName.$extension"
}


private operator fun List<String>.component1() = this[0]
private operator fun List<String>.component2() = this[1]
