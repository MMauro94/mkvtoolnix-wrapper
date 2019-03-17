package com.github.mmauro94.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.io.File

internal object FileConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == File::class.java

    override fun fromJson(jv: JsonValue) = jv.string?.let { File(it) }

    override fun toJson(value: Any): String {
        return if (value is File) "\"" + value.absolutePath.toString() + "\""
        else throw KlaxonException("Must be File")
    }
}