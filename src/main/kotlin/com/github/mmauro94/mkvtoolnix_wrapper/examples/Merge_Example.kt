package com.github.mmauro94.mkvtoolnix_wrapper.examples

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.add
import java.io.File
import java.time.Duration


fun main() {
    MkvToolnix.merge(File("output.mkv"))
        .globalOptions {
            additionalArgs.add("--debug", "something") //Add some non supported global options
        }
        .addInputFile(File("input1.mkv")) {
            subtitleTracks.excludeAll() //Exclude all subtitles tracks from this file
            additionalArgs.add("--colour-matrix", "0:1") //Additional non supported options
        }
        .addInputFile(File("input2.mkv")) {
            videoTracks.excludeAll() //Exclude all video tracks from this file
            editTrackById(2) {
                //Edit track with ID 2 from this file
                language("eng") //Set eng language
                isDefault = true //Set default track
                isForced = false //Set NOT forced track
                name = "¯\\_(ツ)_/¯" //Set track name
                sync(Duration.ofMillis(-250)) //Set sync offset
            }
        }
        .outputControl {
            //Change track output order
            trackOrder.add(0 to 0)
            trackOrder.add(1 to 0)
        }
        .executeAndPrint()
}