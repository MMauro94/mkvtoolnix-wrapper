package com.github.mmauro94.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.awt.Dimension

internal object DimensionConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Dimension::class.java

    override fun fromJson(jv: JsonValue) = jv.string?.let {
        it.split("x").let { tokens ->
            Dimension(tokens[0].toInt(), tokens[1].toInt())
        }
    }

    override fun toJson(value: Any): String {
        return if (value is Dimension) "\"" + value.width + "x" + value.height + "\""
        else throw KlaxonException("Must be Dimension")
    }
}