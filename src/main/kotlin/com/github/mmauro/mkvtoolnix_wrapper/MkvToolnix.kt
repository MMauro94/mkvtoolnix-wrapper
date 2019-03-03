package com.github.mmauro.mkvtoolnix_wrapper

import java.io.File

object MkvToolnix {

    fun identify(file: File) = MkvToolnixFileIdentification.identify(file)
}