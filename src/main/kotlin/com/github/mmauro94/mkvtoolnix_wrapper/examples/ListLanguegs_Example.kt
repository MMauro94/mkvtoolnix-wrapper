package com.github.mmauro94.mkvtoolnix_wrapper.examples

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixLanguage


fun main() {
    //MkvToolnixLanguage.all is a lazy property which will be evaluated on first call
    MkvToolnixLanguage.all.values.toList().sortedBy { it.name }.forEach {
        println(it)
    }
}