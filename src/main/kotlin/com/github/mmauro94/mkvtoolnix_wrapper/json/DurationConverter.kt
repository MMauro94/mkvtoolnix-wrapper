package com.github.mmauro94.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.Duration

internal object DurationConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Duration::class.java

    override fun fromJson(jv: JsonValue) = jv.longValue?.let { Duration.ofNanos(it) }

    override fun toJson(value: Any): String {
        return if (value is Duration) "\"" + value.toNanos().toString() + "\""
        else throw KlaxonException("Must be Duration")
    }
}