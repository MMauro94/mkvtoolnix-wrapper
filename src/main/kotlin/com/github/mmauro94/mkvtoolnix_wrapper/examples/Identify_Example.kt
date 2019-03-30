package com.github.mmauro94.mkvtoolnix_wrapper.examples

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import java.io.File


fun main() {
    val file = File("myfile.mkv")
    MkvToolnix.identify(file).apply {
        println(file.absolutePath)
        container.properties?.title?.let {
            println("Title: $it")
        }
        container.properties?.duration?.let {
            println("Duration: $it")
        }
        println()

        tracks.forEach { t ->
            //Print track information
            print("Track id ${t.id}: ${t.type.name}")
            t.properties?.language?.let { println(", $it") }
            t.properties?.codecId?.let { print(", $it") }
            t.properties?.trackName?.let { println(", $it") }
        }
    }

}