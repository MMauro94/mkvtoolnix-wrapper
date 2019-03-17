package com.github.mmauro94.mkvtoolnix_wrapper

import com.beust.klaxon.Json

data class MkvToolnixChapter(
    @Json("num_entries")
    val numEntries: Int
)