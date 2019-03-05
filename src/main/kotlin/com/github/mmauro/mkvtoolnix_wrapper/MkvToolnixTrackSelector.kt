package com.github.mmauro.mkvtoolnix_wrapper

import com.github.mmauro.mkvtoolnix_wrapper.propedit.MkvPropEditPropertyEditSelector
import java.math.BigInteger

sealed class MkvToolnixTrackSelector : MkvPropEditPropertyEditSelector, CommandArg {

    class TrackPositionSelector(val position: Int, val type: MkvToolnixTrackType? = null) : MkvToolnixTrackSelector() {
        override fun commandArg() =
            if (type === null) {
                "track:$position"
            } else {
                "track:${type.singleLetterAbbreviation}$position"
            }
    }

    class TrackUidSelector(val uid: BigInteger) : MkvToolnixTrackSelector() {
        override fun commandArg() = "track:=$uid"
    }

    class TrackNumberSelector(val number: Int) : MkvToolnixTrackSelector() {
        override fun commandArg() = "track:@$number"
    }

    companion object {
        @JvmStatic
        fun ofTrack(track: MkvToolnixTrack) =
            when {
                track.properties?.uid != null -> TrackUidSelector(
                    track.properties.uid
                )
                track.properties?.number != null -> TrackNumberSelector(
                    track.properties.number
                )
                else -> TrackPositionSelector(track.trackPosition)
            }
    }
}