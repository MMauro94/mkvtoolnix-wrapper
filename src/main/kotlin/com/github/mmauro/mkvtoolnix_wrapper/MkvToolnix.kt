package com.github.mmauro.mkvtoolnix_wrapper

import java.io.File

object MkvToolnix {

    fun identify(file: File) = MkvToolnixFileIdentification.identify(file)

}

internal fun String.mkvtoolnixEscape() : String {
    return replace(" ", "\\s")
        .replace("\"", "\\2")
        .replace(":", "\\c")
        .replace("#", "\\h")
        .replace("\\", "\\\\")
}