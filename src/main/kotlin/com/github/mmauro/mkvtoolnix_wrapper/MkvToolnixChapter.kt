package com.github.mmauro.mkvtoolnix_wrapper

import com.beust.klaxon.Json

data class MkvToolnixChapter(
    @Json("num_entries")
    val numEntries: Int
)