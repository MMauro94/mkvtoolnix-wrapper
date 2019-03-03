package com.github.mmauro.mkvtoolnix_wrapper.json

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import com.github.mmauro.mkvtoolnix_wrapper.MkvToolnixLanguage

internal object MkvToolnixLanguageConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == MkvToolnixLanguage::class.java

    override fun fromJson(jv: JsonValue) =
        jv.string?.let { MkvToolnixLanguage.all[it] ?: throw KlaxonException("Invalid language code $it") }

    override fun toJson(value: Any): String {
        return if (value is MkvToolnixLanguage) "\"" + value.iso639_2 + "\""
        else throw KlaxonException("Must be MkvToolnixLanguage")
    }
}