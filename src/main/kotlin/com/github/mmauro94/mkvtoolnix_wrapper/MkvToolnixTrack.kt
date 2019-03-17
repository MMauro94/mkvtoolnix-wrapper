package com.github.mmauro94.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import com.github.mmauro94.mkvtoolnix_wrapper.json.KlaxonBigInteger
import java.awt.Dimension
import java.math.BigInteger
import java.time.Duration

data class MkvToolnixTrack(
    @Json("codec")
    val codec: String,

    @Json("id")
    val id: Long,

    @Json("type")
    val type: MkvToolnixTrackType,

    @Json("properties")
    val properties: Properties?
) {

    @Json(ignored = true)
    internal var _fileIdentification: MkvToolnixFileIdentification? = null

    @Json(ignored = true)
    internal var _trackPosition : Int? = null


    /** The [MkvToolnixFileIdentification] this track belongs to */
    @Json(ignored = true)
    val fileIdentification by lazy { _fileIdentification!! }

    /** 1-index position of the track in the source file */
    @Json(ignored = true)
    val trackPosition by lazy { _trackPosition!! }



    fun isDefault(default: Boolean) = properties?.defaultTrack ?: default

    fun isDefault(): Boolean? = properties?.defaultTrack


    fun isEnabled(default: Boolean) = properties?.enabledTrack ?: default

    fun isEnabled(): Boolean? = properties?.enabledTrack


    fun isForced(default: Boolean) = properties?.forcedTrack ?: default

    fun isForced(): Boolean? = properties?.forcedTrack


    data class Properties(
        @Json("aac_is_abr")
        val aacIsAbr: AacIsAbr? = null,

        @Json("audio_bits_per_sample")
        val audioBitsPerSample: Int? = null,

        @Json("audio_channels")
        val audioChannels: Int? = null,

        @Json("audio_sampling_frequency")
        val audioSamplingFrequency: Int? = null,

        @Json("codec_delay")
        val codecDelay: Int? = null,

        @Json("codec_id")
        val codecId: String? = null,

        @Json("codec_private_data")
        val codecPrivateData: String? = null,

        @Json("codec_private_length")
        val codecPrivateLength: Int? = null,

        @Json("codec_encoding_algorithms")
        val codecEncodingAlgorithms: String? = null,

        @Json("default_location")
        val defaultDuration: Duration? = null,

        @Json("default_track")
        val defaultTrack: Boolean? = null,

        @Json("display_dimensions")
        val displayDimensions: Dimension? = null,

        @Json("display_unit")
        val displayUnit: Int? = null,

        @Json("enabled_track")
        val enabledTrack: Boolean? = null,

        @Json("encoding")
        val encoding: String? = null,

        @Json("forced_track")
        val forcedTrack: Boolean? = null,

        @Json("language")
        val language: MkvToolnixLanguage? = null,

        @Json("minimum_timestamp")
        val minimumTimestamp: Duration? = null,

        @Json("multiplexed_tracks")
        val multiplexedTracks: List<Int>? = null,

        @Json("number")
        val number: Int? = null,

        @Json("packetizer")
        val packetizer: String? = null,

        @Json("pixel_dimensions")
        val pixelDimensions: Dimension? = null,

        @Json("program_number")
        val programNumber: Int? = null,

        @Json("stereo_mode")
        val stereoMode: Int? = null,

        @Json("stream_id")
        val streamId: Int? = null,

        @Json("sub_stream_id")
        val subStreamId: Int? = null,

        @Json("tag_artisy")
        val tagArtist: String? = null,

        @Json("tag_bitsps")
        val tagBitsps: String? = null,

        @Json("tag_bps")
        val tagBps: String? = null,

        @Json("tag_fps")
        val tagFps: String? = null,

        @Json("tag_title")
        val tagTitle: String? = null,

        @Json("tag_teletext_page")
        val tagTeletextPage: Int? = null,

        @Json("text_subtitles")
        val textSubtitles: Boolean? = null,

        @Json("track_name")
        val trackName: String? = null,

        @Json("uid")
        @KlaxonBigInteger
        val uid: BigInteger? = null
    ) {

        enum class AacIsAbr(val value: Boolean?) {
            TRUE(true), FALSE(false), UNKNOWN(null)
        }
    }
}


