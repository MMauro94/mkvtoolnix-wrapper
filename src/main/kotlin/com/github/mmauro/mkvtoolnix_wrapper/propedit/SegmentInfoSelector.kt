package com.github.mmauro.mkvtoolnix_wrapper.propedit

/**
 * Represents the selector to select the segment information in a `--edit` command.
 *
 * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.edit_selectors.segment_info)
 */
object SegmentInfoSelector : MkvPropEditPropertyEditSelector {
    override fun commandArgs() = listOf("segment_info")
}