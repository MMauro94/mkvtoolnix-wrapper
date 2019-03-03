package com.github.mmauro.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime

data class MkvToolnixContainer(
    @Json("recognized")
    val recognized: Boolean,

    @Json("supported")
    val supported: Boolean,

    @Json("type")
    val type: String? = null,

    @Json("properties")
    val properties: Properties? = null
) {

    data class Properties(
        @Json("container_type")
        val containerType: Int? = null,

        @Json("date_local")
        val dateLocal: ZonedDateTime? = null,

        @Json("date_utc")
        val dateUtc: Instant? = null,

        @Json("duration")
        val duration: Duration?,

        @Json("is_providing_timestamps")
        val isProvidingTimestamps: Boolean? = null,

        @Json("muxing_application")
        val muxingApplication: String? = null,

        @Json("next_segment_uid")
        val nextSegmentUid: String? = null,

        @Json("other_file")
        val otherFile: List<String>? = null,

        @Json("playlist")
        val playlist: Boolean? = null,

        @Json("playlist_chapter")
        val playlistChapter: Int? = null,

        @Json("playlist_duration")
        val playlistDuration: Duration? = null,

        @Json("playlist_file")
        val playlistFile: List<String>? = null,

        @Json("playlist_size")
        val playlistSize: Int? = null,

        @Json("previoust_segment_uid")
        val previousSegmentUid: String? = null,

        @Json("programs")
        val programs: List<Program>? = null,

        @Json("segment_uid")
        val segmentUid: String? = null,

        @Json("title")
        val title: String? = null,

        @Json("writing_application")
        val writingApplication: String? = null
    ) {
        data class Program(
            @Json("program_number")
            val programNumber: Int? = null,

            @Json("service_name")
            val serviceName: String? = null,

            @Json("service_provider")
            val serviceProvider: String? = null
        )
    }
}