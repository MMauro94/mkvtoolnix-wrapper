package com.github.mmauro94.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import com.github.mmauro94.mkvtoolnix_wrapper.json.klaxon
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditCommand
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditParseMode
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

data class MkvToolnixFileIdentification(
    @Json("attachments")
    val attachments: List<MkvToolnixAttachment>,

    @Json("chapters")
    val chapters: List<MkvToolnixChapter>,

    @Json("container")
    val container: MkvToolnixContainer,

    @Json("errors")
    val errors: List<String>,

    @Json("file_name")
    val fileName: File,

    @Json("tracks")
    val tracks: List<MkvToolnixTrack>,

    @Json("warnings")
    val warnings: List<String>
) {

    /**
     * @return a new [MkvPropEditCommand] with this file as a subject
     */
    fun propedit() = MkvPropEditCommand(fileName)

    companion object {

        /**
         * Same as [MkvToolnix.identify]
         */
        @JvmStatic
        fun identify(file: File): MkvToolnixFileIdentification {
            val p = MkvToolnixBinary.MKV_MERGE.processBuilder("-J", file.absolutePath).start()
            return BufferedReader(InputStreamReader(p.inputStream)).use { input ->
                klaxon().parse<MkvToolnixFileIdentification>(input)!!.apply {
                    tracks.forEachIndexed { index, t ->
                        t._fileIdentification = this
                        t._trackPosition = index + 1
                    }
                }
            }
        }
    }
}