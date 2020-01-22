package com.github.mmauro94.mkvtoolnix_wrapper.extract

import com.github.mmauro94.mkvtoolnix_wrapper.AdditionalArgs
import com.github.mmauro94.mkvtoolnix_wrapper.CommandArgs
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixTrack
import com.github.mmauro94.mkvtoolnix_wrapper.add
import java.io.File

class MkvExtractTrackExtractionMode : MkvExtractExtractionMode {

    //region OPTIONS
    val options = Options()

    class Options : CommandArgs {

        var blockAdd: Int? = null
        var cuesheet: Boolean = false

        val additionalArgs = AdditionalArgs()

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            if (blockAdd != null) {
                add("--blockadd")
                add(blockAdd.toString())
            }
            if (cuesheet) {
                add("--cuesheet")
            }
            add(additionalArgs)
        }
    }

    /**
     * @param f lambda that changes the options
     */
    fun options(f: Options.() -> Unit) = apply {
        f(options)
    }
    //endregion


    val tracks: MutableList<Pair<Long, File>> = ArrayList()

    fun addTrack(track: MkvToolnixTrack, file: File) {
        addTrackId(track.id, file)
    }

    fun addTrackId(trackId: Long, file: File) {
        tracks.add(trackId to file)
    }


    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add("tracks")
        add(options)
        tracks.forEach {(id,file)->
            add("$id:${file.absolutePath}")
        }
    }

}