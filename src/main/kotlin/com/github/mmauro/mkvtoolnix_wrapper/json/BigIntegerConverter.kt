package com.github.mmauro.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.math.BigInteger

internal object BigIntegerConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == BigInteger::class.java

    override fun fromJson(jv: JsonValue) = jv.bigInteger

    override fun toJson(value: Any): String {
        return if (value is BigInteger) value.toString()
        else throw KlaxonException("Must be BigInteger")
    }
}