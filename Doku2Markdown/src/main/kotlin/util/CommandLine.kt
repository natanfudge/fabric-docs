package util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun getCommandOutput(params: List<String>): String {
    val pb = ProcessBuilder(params)
    val p: Process
    var result = ""
    p = pb.start()
    val reader = BufferedReader(InputStreamReader(p.inputStream))

    val sj = StringJoiner(System.getProperty("line.separator"))
    reader.lines().iterator().forEachRemaining { sj.add(it) }
    result = sj.toString()

    p.waitFor()
    p.destroy()

    return result
}