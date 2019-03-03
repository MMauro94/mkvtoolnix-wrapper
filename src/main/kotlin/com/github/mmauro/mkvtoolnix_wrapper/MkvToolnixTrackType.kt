package com.github.mmauro.mkvtoolnix_wrapper

/**
 * The type of a track
 */
enum class MkvToolnixTrackType(val singleLetterAbbreviation : Char) {
    audio('a'),
    video('v'),
    button('b'),
    subtitles('s');

}