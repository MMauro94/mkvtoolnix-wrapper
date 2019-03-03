package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.CommandArgs

enum class ParseMode(val mode : String) : CommandArgs {
    FAST("fast"), FULL("full");

    override fun commandArgs() = listOf("--parse-mode", mode)
}