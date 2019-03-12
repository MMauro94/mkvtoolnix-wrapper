package com.github.mmauro.mkvtoolnix_wrapper

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * A binary included with MKV Toolnix
 * @param binaryName the name of the binary
 */
enum class MkvToolnixBinary(val binaryName: String) {
    MKV_PROP_EDIT("mkvpropedit"),
    MKV_MERGE("mkvmerge");

    /**
     * The file pointing to this binary, or `null` if the the binary should be searched using the environment PATH
     */
    fun file() = MkvToolnix.mkvToolnixPath?.let {
        File(it, binaryName)
    }

    internal fun command() = file()?.toString() ?: binaryName

    internal fun processBuilder(vararg params: String) = ProcessBuilder(command(), *params).apply {
        redirectErrorStream(true)
    }

    /**
     * Detects the version of this binary
     * @return the complete string with the version
     */
    fun getVersionString() = processBuilder("--version").start().let {
        BufferedReader(InputStreamReader(it.inputStream)).use { input ->
            input.readText().trim()
        }
    }

    /**
     * Detects and parses the version of this binary
     * @return a [VersionInfo] instance containing the parsed information
     */
    fun getVersionInfo(): VersionInfo {
        val m = VERSION_PATTERN.matcher(getVersionString())
        return if (m.matches()) {
            VersionInfo(
                programName = m.group(1)!!,
                version = Version(m.group(2)!!.toInt(), m.group(3)!!.toInt(), m.group(4)!!.toInt()),
                codename = m.group(5)!!,
                is64bit = m.group(6) == "64"
            )
        } else {
            throw IllegalStateException("Invalid version string")
        }
    }

    companion object {
        private val VERSION_PATTERN = Pattern.compile("^(.+)\\s+v(\\d+)\\.(\\d+)\\.(\\d+)\\s+\\('(.+)'\\)(?:\\s+(\\d+)-bit)?$")!!
    }
}