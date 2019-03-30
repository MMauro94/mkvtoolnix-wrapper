package com.github.mmauro94.mkvtoolnix_wrapper

/**
 * Interface that represents a class that is able to generate a single command argument
 */
interface CommandArg : CommandArgs {
    /**
     * @return the command arg
     */
    fun commandArg(): String

    /**
     * Default implemented that returns a singleton list containing the single [commandArg]
     */
    override fun commandArgs() = listOf(commandArg())
}

/**
 * Interface that represents a class that is able to generate a list of command arguments
 */
interface CommandArgs {

    /**
     * @return the list of command arguments
     */
    fun commandArgs(): List<String>
}

class AdditionalArgs(
    private val args: MutableList<String> = mutableListOf()
) : MutableList<String> by args, CommandArgs {
    override fun commandArgs() = args
}

/**
 * Extension function to add a [CommandArgs] to a list of strings
 */
fun MutableList<String>.add(args: CommandArgs) {
    addAll(args.commandArgs())
}

/**
 * Extension function to add a vararg strings to a list
 */
fun MutableList<String>.add(vararg args: String) {
    addAll(args)
}