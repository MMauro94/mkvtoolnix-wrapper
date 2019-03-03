package com.github.mmauro.mkvtoolnix_wrapper

import com.github.mmauro.mkvtoolnix_wrapper.propedit.setIsDefault
import com.github.mmauro.mkvtoolnix_wrapper.propedit.setLanguage
import java.io.File

fun main() {
    /*MkvToolnixLanguage.all.forEach {
        println(it)
    }
    println(MkvToolnixLanguage.all.size)*/


    val file =
        File("C:\\Users\\molin\\Desktop\\TO SERVER\\twd\\The.Walking.Dead.8x10.Hai.Gia.Perso.ITA.ENG.1080p.WEB-DLMux.H264-Morpheus.mkv")

    val x = MkvToolnix.identify(file)

    x.propedit().apply {
        x.tracks.forEach { track ->
            addTrackEdit(track).setLanguage(MkvToolnixLanguage.all["eng"]!!).setIsDefault(Math.random() > 0.5)
        }
        println(commandArgs().joinToString(" "))
    }


    //TODO check out https://mkvtoolnix.download/doc/mkvmerge-identification-output-schema-v11.json
}