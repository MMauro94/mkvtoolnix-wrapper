package com.github.mmauro94.mkvtoolnix_wrapper.json

import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon

internal fun klaxon() = Klaxon()
    .converter(ZonedDateTimeConverter)
    .converter(InstantConverter)
    .converter(DurationConverter)
    .converter(FileConverter)
    .converter(DimensionConverter)
    .converter(MkvToolnixLanguageConverter)
    .fieldConverter(KlaxonBigInteger::class, BigIntegerConverter)


internal fun JsonValue.long() : Long? = int?.toLong() ?: longValue