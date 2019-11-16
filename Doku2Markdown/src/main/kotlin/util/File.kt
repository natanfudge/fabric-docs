package util

import java.io.File

fun File.copyContentsRecursively(target: File, filter: (File) -> Boolean = { true }) {
    require(isDirectory)
    for (file in walkTopDown()) {
        if (file.isDirectory) continue
        if (!filter(file)) continue
        val relativePath = file.relativeTo(this)
        val destPath = File(target.path + "/" + relativePath.path)
        file.copyTo(destPath, overwrite = true)
    }
}