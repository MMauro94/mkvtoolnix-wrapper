package com.github.mmauro.mkvtoolnix_wrapper

interface CommandArg : CommandArgs {
    fun commandArg() : String

    override fun commandArgs() = listOf(commandArg())
}

interface CommandArgs {

    fun commandArgs() : List<String>
}

fun MutableList<String>.add(args: CommandArgs) {
    addAll(args.commandArgs())
}

internal fun MutableList<String>.add(vararg args: String) {
    addAll(args)
}