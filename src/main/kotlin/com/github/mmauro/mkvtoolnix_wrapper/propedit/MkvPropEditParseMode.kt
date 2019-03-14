package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.CommandArgs

/**
 * The parse mode used in `mkvpropedit`
 *
 *
 * The [FAST] mode does not parse the whole file but uses the meta seek elements for locating the required elements of a source file. In 99% of all cases this is enough. But for files that do not contain meta seek elements or which are damaged the user might have to set the [FULL] parse mode.
 *
 * A full scan of a file can take a couple of minutes while a fast scan only takes seconds.
 *
 * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.description.parse_mode)
 */
enum class MkvPropEditParseMode(val mode : String) : CommandArgs {
    /**
     * Scans only meta seek elements to scan the file
     *
     * See [MkvPropEditParseMode] doc for more info.
     */
    FAST("fast"),
    /**
     * Scans the whole file to determine its contents.
     *
     * See [MkvPropEditParseMode] doc for more info.
     */
    FULL("full");

    override fun commandArgs() = listOf("--parse-mode", mode)
}