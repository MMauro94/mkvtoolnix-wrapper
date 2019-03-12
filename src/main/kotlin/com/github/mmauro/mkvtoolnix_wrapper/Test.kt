package com.github.mmauro.mkvtoolnix_wrapper

import com.github.mmauro.mkvtoolnix_wrapper.propedit.deleteName
import com.github.mmauro.mkvtoolnix_wrapper.propedit.setLanguage
import java.io.File

fun main() {
    /*MkvToolnixLanguage.all.forEach {
        println(it)
    }
    println(MkvToolnixLanguage.all.size)*/


    val file =
        File("C:\\Users\\molin\\Desktop\\test.mkv")

    val x = MkvToolnix.identify(file)

    val result = x.propedit().apply {
        x.tracks.forEach {
            editTrack(it) {
                deleteName()
            }
        }
        x.tracks.asSequence().filter { it.type == MkvToolnixTrackType.video }.forEach {
            editTrack(it) {
                setLanguage("und")
            }
        }
    }.executeAndPrint()

    /*
    println(x.propedit()
        .editTrackPosition(1) {
            setLanguage("eng")
            deleteName()
        }
        .editTrackNumber(1) {
            setIsDefault(false)
        }
        .editTrack(x.tracks[0]) {
            setIsDefault(true)
        }
        .addAttachment(File("lol")) {
            name = "pincopallo"
        }
        .updateAttachmentId(75) {
            name = "cioo"
        }
        .deleteAttachmentName("fff")
        .commandArgs()
        .joinToString(" ")
    )

    println()
    println(MkvToolnixBinary.MKV_PROP_EDIT.getVersionString())
    println(MkvToolnixBinary.MKV_PROP_EDIT.getVersionInfo())
    println()

    println(MkvMergeCommand(File("out.mkv"))
        .addInputFile(File("inputA.avi")) {
            audioTracks.excludeAll()
            subtitleTracks.include {
                addByLanguage("ita")
            }
            videoTracks.excludeAll()
        }
        .addInputFile(File("inputB.avi")) {
            audioTracks.include {
                addById(4)
            }
            videoTracks.includeAll()
            subtitleTracks.excludeAll()
        }
        .commandArgs()
        .joinToString(" "))*/

    //TODO check out https://mkvtoolnix.download/doc/mkvmerge-identification-output-schema-v11.json

    //TODO checkout http://manpages.ubuntu.com/manpages/bionic/pl/man1/mkvpropedit.1.html, section ESCAPING SPECIAL CHARS IN TEXT

}