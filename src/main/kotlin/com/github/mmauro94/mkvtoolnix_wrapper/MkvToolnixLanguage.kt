package com.github.mmauro94.mkvtoolnix_wrapper

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * A language recognized by MKV Toolnix
 *
 * @param name the english name of the language
 * @param iso639_3 the three-letter ISO 639-3 code
 * @param iso639_2 the three-letter ISO 639-2 code. Can be null
 * @param iso639_1 the two-letter ISO 639-1 code. Can be null
 */
class MkvToolnixLanguage internal constructor(
    val name: String,
    val iso639_3: String,
    val iso639_2: String?,
    val iso639_1: String?
) {

    override fun equals(other: Any?) = other is MkvToolnixLanguage && other.iso639_3 == iso639_3

    override fun hashCode() = iso639_3.hashCode()

    override fun toString() = "$name ($iso639_3)"

    fun isUndefined() = iso639_3 == "und"

    fun isEnglish() = iso639_3 == "eng"

    companion object {

        private val LANGUAGE_LINE_PATTERN = "^\\s*([^|]+)\\s+\\|\\s*([a-z]{3})\\s*\\|\\s*([a-z]{3})?\\s*\\|\\s*([a-z]{2})?\\s*$".toRegex()

        val english by lazy {
            all.getValue("eng")
        }

        val undefined by lazy {
            all.getValue("und")
        }

        /**
         * Map of all the available languages in `mkvmerge`
         * This value is lazily evaluated the first time it is used.
         */
        val all: Map<String, MkvToolnixLanguage> by lazy {
            val p = MkvToolnixBinary.MKV_MERGE.processBuilder("--list-languages").start()
            BufferedReader(InputStreamReader(p.inputStream)).use { input ->
                HashMap<String, MkvToolnixLanguage>(350).apply {
                    for (line in input.lines().skip(2)) {
                        val match = LANGUAGE_LINE_PATTERN.matchEntire(line)
                        val lang = when {
                            match != null -> {
                                 val (name, iso3, iso2, iso1) = match.destructured
                                MkvToolnixLanguage(
                                    name = name.trim(),
                                    iso639_3 = iso3,
                                    iso639_2 = iso2.ifBlank { null },
                                    iso639_1 = iso1.ifBlank { null }
                                )
                            }
                            else -> null
                        }
                        if (lang != null) {
                            put(lang.iso639_3, lang)
                        }
                    }
                }
            }
        }
    }
}

fun MkvToolnixLanguage?.isNullOrUndefined() = this == null || this.isUndefined()