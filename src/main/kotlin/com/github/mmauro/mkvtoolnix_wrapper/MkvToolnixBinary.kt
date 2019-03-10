package com.github.mmauro.mkvtoolnix_wrapper

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.util.regex.Pattern

/**
 * A binary included with MKV Toolnix
 * @param executableName the name of the binary
 */
enum class MkvToolnixBinary(val executableName: String) {
    MKV_PROP_EDIT("mkvpropedit"),
    MKV_MERGE("mkvmerge");

    internal fun processBuilder(vararg params: String) = ProcessBuilder(executableName, *params).apply {
        redirectErrorStream(true)
    }

    fun getVersionString() = processBuilder("--version").start().let {
        BufferedReader(InputStreamReader(it.inputStream)).use { input ->
            input.readText().trim()
        }
    }

    fun getVersionInfo() : VersionInfo {
        val m =  VERSION_PATTERN.matcher(getVersionString())
        return if(m.matches()) {
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