package com.github.mmauro94.mkvtoolnix_wrapper.merge

import com.github.mmauro94.mkvtoolnix_wrapper.*
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixCommandException.MkvMergeException
import com.github.mmauro94.mkvtoolnix_wrapper.utils.toInt
import java.io.File
import java.time.Duration

class MkvMergeCommand(val outputFile: File) : MkvToolnixCommand<MkvMergeCommand>(MkvToolnixBinary.MKV_MERGE) {

    object GlobalOptions : CommandArgs {

        var verbose = false
        var webm = false
        var title: String? = null
        var defaultLanguage: MkvToolnixLanguage? = null

        fun defaultLanguage(language: String) {
            defaultLanguage = MkvToolnixLanguage.all.getValue(language)
        }

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            if (verbose) {
                add("--verbose")
            }
            if (webm) {
                add("--webm")
            }
            title?.let {
                add("--title")
                add(it)
            }
        }
    }

    class InputFile(val file: File) : CommandArgs {

        class CopyTracksCommand(val typeCommand: String, val excludeAllCommand: String) : CommandArgs {

            enum class Mode { INCLUDE, EXCLUDE }

            var mode = Mode.EXCLUDE
            val tracks = Tracks()

            sealed class Track {

                abstract fun partialArg(): String

                class TrackId(val id: Long) : Track() {
                    override fun partialArg() = id.toString()
                }

                class TrackLanguage(val language: MkvToolnixLanguage) : Track() {

                    constructor(language: String) : this(MkvToolnixLanguage.all.getValue(language))

                    override fun partialArg() = language.iso639_2
                }

                companion object {
                    fun of(track: MkvToolnixTrack) = TrackId(track.id)
                }
            }

            class Tracks {
                val tracks: MutableList<Track> = ArrayList()

                fun addById(id: Long) = apply { tracks.add(Track.TrackId(id)) }
                fun addByLanguage(language: String) = apply { tracks.add(Track.TrackLanguage(language)) }
                fun addByLanguage(language: MkvToolnixLanguage) = apply { tracks.add(Track.TrackLanguage(language)) }
            }

            fun excludeAll() = apply {
                //Excluding all means including nothing
                tracks.tracks.clear()
                mode = Mode.INCLUDE
            }

            fun includeAll() = apply {
                //Including all means excluding nothing
                tracks.tracks.clear()
                mode = Mode.EXCLUDE
            }

            fun include(f: Tracks.() -> Unit) = apply {
                excludeAll() //Include nothing
                tracks.apply(f)
            }

            fun exclude(f: Tracks.() -> Unit) = apply {
                includeAll() //Exclude nothing
                tracks.apply(f)
            }


            override fun commandArgs(): List<String> = ArrayList<String>().apply {
                if (mode == Mode.INCLUDE && tracks.tracks.isEmpty()) {
                    add(excludeAllCommand)
                } else if (!tracks.tracks.isEmpty()) {
                    add(typeCommand)
                    add(StringBuilder().apply {
                        if (mode == Mode.EXCLUDE) {
                            append('!')
                        }
                        append(tracks.tracks.joinToString(",") { it.partialArg() })
                    }.toString())
                }
            }
        }

        class TrackOption(val trackId: Long) : CommandArgs {

            val isEditingAllTracks = trackId == -2L

            class Sync(val offset: Duration, val linearDriftFixRatio: Pair<Float, Float?>?) {

                fun arg(trackId: Long) = arrayOf(
                    "--sync",
                    StringBuilder().apply {
                        append("$trackId:${offset.toMillis()}")
                        if (linearDriftFixRatio != null) {
                            val (o, p) = linearDriftFixRatio
                            append(",$o")
                            if (p != null) {
                                append("/$p")
                            }
                        }
                    }.toString()
                )
            }

            var sync: Sync? = null
            var isDefault: Boolean? = null
            var isForced: Boolean? = null
            var name: String? = null
            var language: MkvToolnixLanguage? = null


            fun sync(offset: Duration, linearDriftFixRatio: Pair<Float, Float?>? = null) = apply {
                sync = Sync(offset, linearDriftFixRatio)
            }

            fun language(iso639_2: String) = apply {
                language = MkvToolnixLanguage.all.getValue(iso639_2)
            }

            override fun commandArgs(): List<String> = ArrayList<String>().apply {
                sync?.let {
                    add(*it.arg(trackId))
                }
                isDefault?.let {
                    add("--default-track")
                    add("$trackId:${it.toInt()}")
                }
                isForced?.let {
                    add("--forced-track")
                    add("$trackId:${it.toInt()}")
                }
                name?.let {
                    add("--track-name")
                    add("$trackId:$it")
                }
                language?.let {
                    add("--language")
                    add("$trackId:${it.iso639_2}")
                }
            }
        }


        val videoTracks = CopyTracksCommand("--video-tracks", "--no-audio")
        val audioTracks = CopyTracksCommand("--audio-tracks", "--no-video")
        val subtitleTracks = CopyTracksCommand("--subtitle-tracks", "--no-subtitles")
        val buttonTracks = CopyTracksCommand("--button-track", "--no-buttons")
        val trackTags = CopyTracksCommand("--track-tags", "--no-track-tags")

        val trackOptions: MutableMap<Long, TrackOption> = HashMap()

        fun editTrackById(trackId: Long, f: TrackOption.() -> Unit) = apply {
            trackOptions.getOrPut(trackId) { TrackOption(trackId) }.apply(f)
        }

        fun editTrack(track: MkvToolnixTrack, f: TrackOption.() -> Unit) = editTrackById(track.id, f)

        fun editAllTracks(f: TrackOption.() -> Unit) = editTrackById(-2, f)

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            add(videoTracks)
            add(audioTracks)
            add(subtitleTracks)
            add(buttonTracks)
            add(trackTags)
            trackOptions.values.forEach { add(it) }
            add(file.absolutePath)
        }

    }

    val inputFiles: MutableList<InputFile> = ArrayList()

    fun addInputFile(file: File, f: InputFile.() -> Unit = {}) = apply {
        inputFiles.add(InputFile(file).apply(f))
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add(GlobalOptions)
        add("--output", outputFile.absolutePath.toString())
        inputFiles.forEach { add(it) }
    }

    override fun me() = this

    override val exceptionInitializer = ::MkvMergeException
}