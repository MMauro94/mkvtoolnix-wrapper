package com.github.mmauro.mkvtoolnix_wrapper

import com.github.mmauro.mkvtoolnix_wrapper.propedit.MkvPropEditPropertyEditSelector
import java.math.BigInteger

/**
 * Sealed class that represents a way to select a track in a source file
 * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.edit_selectors.track_headers)
 */
sealed class MkvToolnixTrackSelector : MkvPropEditPropertyEditSelector, CommandArg {

    /**
     * Selects a track by its position in the source file.
     * The track order is the same that [MkvToolnix.identify] outputs.
     *
     * Either:
     * * `track:n`, where `n` is the position
     * * `track:tn`, where `t` is the track type, and `n` is the position
     * @param position the track position, 1-index
     * @param type if not null, the position will be referring only to tracks of this type
     */
    class PositionSelector(val position: Int, val type: MkvToolnixTrackType? = null) : MkvToolnixTrackSelector() {
        override fun commandArg() =
            if (type === null) {
                "track:$position"
            } else {
                "track:${type.singleLetterAbbreviation}$position"
            }
    }

    /**
     * Selects a track by its UID.
     * The track UID can be obtained by calling [MkvToolnix.identify].
     *
     * `track:=uid`, where `uid` is the track UID
     * @param uid the uid
     */
    class UidSelector(val uid: BigInteger) : MkvToolnixTrackSelector() {

        override fun commandArg() = "track:=$uid"
    }

    /**
     * Selects a track by its number.
     * The track number can be obtained by calling [MkvToolnix.identify].
     *
     * `track:@number`, where `number` is the track number
     * @param number the track number
     */
    class NumberSelector(val number: Int) : MkvToolnixTrackSelector() {
        override fun commandArg() = "track:@$number"
    }


    companion object {

        /**
         * Returns an instance of [PositionSelector] for the given track, using the following logic:
         * * If the UID is available, it returns a [UidSelector]
         * * If the number is available, it returns a [NumberSelector]
         * * Otherwise, it returns a [PositionSelector] based on the global position of the track in the source file
         */
        @JvmStatic
        fun ofTrack(track: MkvToolnixTrack) =
            when {
                track.properties?.uid != null -> UidSelector(
                    track.properties.uid
                )
                track.properties?.number != null -> NumberSelector(
                    track.properties.number
                )
                else -> PositionSelector(track.trackPosition)
            }
    }
}