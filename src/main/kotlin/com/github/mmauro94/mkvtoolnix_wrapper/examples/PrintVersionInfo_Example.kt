package com.github.mmauro94.mkvtoolnix_wrapper.examples

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixBinary


fun main() {
    MkvToolnixBinary.values().forEach { b ->
        b.getVersionInfo().apply {
            println("Binary: $programName")
            println("Codename: $codename")
            println("Version: $version")
            println("Is 64bit: $is64bit")
            println()
        }
    }

}