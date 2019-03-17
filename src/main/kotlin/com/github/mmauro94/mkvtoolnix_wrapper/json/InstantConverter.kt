package com.github.mmauro94.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.Instant

internal object InstantConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Instant::class.java

    override fun fromJson(jv: JsonValue) = if (jv.string == null) null else Instant.parse(jv.string)

    override fun toJson(value: Any): String {
        return if (value is Instant) "\"" + value.toString() + "\""
        else throw KlaxonException("Must be Instant")
    }
}