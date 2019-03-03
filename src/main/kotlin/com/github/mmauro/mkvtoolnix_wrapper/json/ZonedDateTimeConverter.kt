package com.github.mmauro.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.ZonedDateTime

internal object ZonedDateTimeConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == ZonedDateTime::class.java

    override fun fromJson(jv: JsonValue) = if (jv.string == null) null else ZonedDateTime.parse(jv.string)

    override fun toJson(value: Any): String {
        return if (value is ZonedDateTime) value.toString()
        else throw KlaxonException("Must be ZonedDateTime")
    }
}