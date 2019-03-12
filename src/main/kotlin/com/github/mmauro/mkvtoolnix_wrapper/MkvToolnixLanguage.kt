package com.github.mmauro.mkvtoolnix_wrapper

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * A language recognized by MKV Toolnix
 *
 * @param name the english name of the language
 * @param iso639_1 the two-letter ISO 639-1 code. Can be null
 * @param iso639_2 the three-letter ISO 639-2 code
 */
class MkvToolnixLanguage(
    val name: String,
    val iso639_1: String?,
    val iso639_2: String
) {

    override fun equals(other: Any?) = other is MkvToolnixLanguage && other.iso639_2 == iso639_2

    override fun hashCode() = iso639_2.hashCode()

    override fun toString() = "$name ($iso639_2)"

    companion object {

        private val LANGUAGE_LINE_PATTERN =
            Pattern.compile("^\\s*([^\\s]+)\\s+\\|\\s*([a-z]{3})\\s*\\|\\s*([a-z]{2})?\\s*$")!!

        /**
         * Map of all the available languages in `mkvmerge`
         * This value is lazily evaluated the first time it is used.
         */
        val all: Map<String, MkvToolnixLanguage> by lazy {
            val p = MkvToolnixBinary.MKV_MERGE.processBuilder("--list-languages").start()
            BufferedReader(InputStreamReader(p.inputStream)).use { input ->
                HashMap<String, MkvToolnixLanguage>(350).apply {
                    for (line in input.lines().skip(2)) {
                        val match = LANGUAGE_LINE_PATTERN.matcher(line)
                        if (match.matches()) {
                            MkvToolnixLanguage(match.group(1), match.group(3), match.group(2)).let {
                                put(it.iso639_2, it)
                            }
                        }
                    }
                }
            }
        }
    }
}