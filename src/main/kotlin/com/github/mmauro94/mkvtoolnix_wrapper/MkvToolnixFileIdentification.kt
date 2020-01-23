package com.github.mmauro94.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import com.github.mmauro94.mkvtoolnix_wrapper.json.klaxon
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditCommand
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

data class MkvToolnixFileIdentification(
    @Json("attachments")
    val attachments: List<MkvToolnixAttachment> = emptyList(),

    @Json("chapters")
    val chapters: List<MkvToolnixChapter> = emptyList(),

    @Json("container")
    val container: MkvToolnixContainer,

    @Json("errors")
    val errors: List<String>,

    @Json("file_name")
    val fileName: File,

    @Json("tracks")
    val tracks: List<MkvToolnixTrack> = emptyList(),

    @Json("warnings")
    val warnings: List<String>
) {

    /**
     * @return a new [MkvPropEditCommand] with this file as a subject
     */
    fun propedit() = MkvToolnix.propedit(fileName)

    internal fun applyInfoToTracks() = apply{
        tracks.forEachIndexed { index, t ->
            t._fileIdentification = this
            t._trackPosition = index + 1
        }
    }

    companion object {

        /**
         * Same as [MkvToolnix.identify]
         */
        @JvmStatic
        fun identify(file: File): MkvToolnixFileIdentification {
            val p = MkvToolnixBinary.MKV_MERGE.processBuilder("-J", file.absolutePath).start()
            return BufferedReader(InputStreamReader(p.inputStream)).use { input ->
                klaxon().parse<MkvToolnixFileIdentification>(input)!!.applyInfoToTracks()
            }
        }
    }
}