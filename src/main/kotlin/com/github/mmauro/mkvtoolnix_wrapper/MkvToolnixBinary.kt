package com.github.mmauro.mkvtoolnix_wrapper

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
}