package com.github.mmauro94.mkvtoolnix_wrapper.examples

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.add
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.deleteName
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.setLanguage
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.setName
import java.io.File
import java.math.BigInteger


fun main() {
    MkvToolnix.propedit(File("myfile.mkv"))
        .globalOptions {
            additionalArgs.add("--debug", "something")
        }
        .editTrackPropertiesByNumber(2) {
            //Edit the track with the given number
            setLanguage("eng") //Change language to English
            deleteName() //Remove the name
            add("prop-whatever", "value") //Add a custom property
        }
        .editTrackPropertiesByUid(BigInteger.valueOf(2132213123312)) {
            //Edit the track with the given UID
            setName("Audio for blind people") //Change track name
            setLanguage("ita") //Change language to Italian
        }
        .addAttachment(File("image.png")) {
            //Add an attachment with the following info
            name = "Cool Attachment"
            mimeType = "image/png"
            description = "My super cool image"
        }
        .updateAttachmentById(54) {
            mimeType = "application/json"
        }
        .deleteAttachmentByName("lol")
        .executeAndPrint()
}