package com.github.mmauro.mkvtoolnix_wrapper.propedit

object SegmentInfoSelector : MkvPropEditPropertyEditSelector {
    override fun commandArgs() = listOf("segment_info")
}