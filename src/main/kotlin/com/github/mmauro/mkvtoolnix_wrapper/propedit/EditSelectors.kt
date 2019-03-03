package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.CommandArgs
import com.github.mmauro.mkvtoolnix_wrapper.MkvToolnixTrack
import com.github.mmauro.mkvtoolnix_wrapper.MkvToolnixTrackType
import java.math.BigInteger

interface EditSelector : CommandArgs

object SegmentInfoSelector : EditSelector {
    override fun commandArgs() = listOf("segment_info")
}

sealed class TrackSelector : EditSelector {

    class TrackPositionSelector(val position: Int, val type: MkvToolnixTrackType? = null) : TrackSelector() {
        override fun commandArgs() =
            if (type === null) {
                listOf("track:$position")
            } else {
                listOf("track:${type.singleLetterAbbreviation}$position")
            }
    }

    class TrackUidSelector(val uid: BigInteger) : TrackSelector() {
        override fun commandArgs() = listOf("track:=$uid")
    }

    class TrackNumberSelector(val number: Int) : TrackSelector() {
        override fun commandArgs() = listOf("track:@$number")
    }

    companion object {
        fun ofTrack(track: MkvToolnixTrack) =
            when {
                track.properties?.uid != null -> TrackUidSelector(track.properties.uid)
                track.properties?.number != null -> TrackNumberSelector(track.properties.number)
                else -> TrackPositionSelector(track.trackPosition)
            }
    }
}