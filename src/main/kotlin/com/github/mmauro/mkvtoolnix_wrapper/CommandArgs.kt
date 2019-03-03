package com.github.mmauro.mkvtoolnix_wrapper

interface CommandArgs {

    fun commandArgs() : List<String>
}

fun MutableList<String>.add(args: CommandArgs) {
    addAll(args.commandArgs())
}