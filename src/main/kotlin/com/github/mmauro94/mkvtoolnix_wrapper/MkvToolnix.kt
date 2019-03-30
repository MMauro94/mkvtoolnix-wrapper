package com.github.mmauro94.mkvtoolnix_wrapper

import com.github.mmauro94.mkvtoolnix_wrapper.merge.MkvMergeCommand
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditCommand
import java.io.File

object MkvToolnix {

    /**
     * Calls `mkvmerge --identify` with the the provided file. The JSON result is parsed and a new instance of [MkvToolnixFileIdentification], containing the parsed information
     */
    fun identify(file: File) = MkvToolnixFileIdentification.identify(file)

    /**
     * The path in which to find the MKV Toolnix binaries. Defaults to `null`.
     *
     * When `null` the binaries will be searched in the environment PATH
     */
    var mkvToolnixPath : File? = null

    fun merge(outputFile : File) = MkvMergeCommand(outputFile)

    fun propedit(outputFile : File) = MkvPropEditCommand(outputFile)
}

internal fun String.mkvtoolnixEscape() : String {
    return replace(" ", "\\s")
        .replace("\"", "\\2")
        .replace(":", "\\c")
        .replace("#", "\\h")
        .replace("\\", "\\\\")
}