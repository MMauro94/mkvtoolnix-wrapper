package com.github.mmauro94.mkvtoolnix_wrapper

import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditCommand
import com.github.mmauro94.mkvtoolnix_wrapper.utils.asCachedSequence
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Represents an command for one of the binaries of MKV Toolnix
 * @param binary the binary that this command is linked to
 */
abstract class MkvToolnixCommand<SELF : MkvToolnixCommand<SELF>>(val binary: MkvToolnixBinary) : CommandArgs {

    protected fun processBuilder() = binary.processBuilder(*commandArgs().toTypedArray())

    /**
     * Returns the command and its arguments
     */
    override fun toString(): String {
        return binary.command() + " " + commandArgs().joinToString(" ")
    }

    /**
     * Starts the execution of the command, returning the result object immediately, lazily parsing the output.
     * The output can be iterated while the program is running. Calling [MkvToolnixCommandResult.exitCode] will obviously halt until the command terminates.
     *
     * WARNING! If you call this method, you will be responsible for closing the input stream. You can do so by calling [MkvToolnixCommandResult.Lazy.close] on the returned object. It is therefore suggested to [use] the returned object immediately
     *
     * @return the lazy result
     */
    fun executeLazy(): MkvToolnixCommandResult.Lazy<SELF> {
        val p = processBuilder().apply {
            redirectErrorStream(true)
        }.start()

        val reader = BufferedReader(InputStreamReader(p.inputStream))
        val output = reader.lineSequence().map { line ->
            val (msg, type) = when {
                line.startsWith(WARNING_PREFIX, ignoreCase = true) -> {
                    line.substring(WARNING_PREFIX.length).trimStart() to MkvToolnixCommandResult.Line.Type.WARNING
                }
                line.startsWith(ERROR_PREFIX, ignoreCase = true) -> {
                    line.substring(ERROR_PREFIX.length).trimStart() to MkvToolnixCommandResult.Line.Type.ERROR
                }
                else -> line to MkvToolnixCommandResult.Line.Type.INFO
            }
            if (type != MkvToolnixCommandResult.Line.Type.INFO || msg.isNotBlank()) {
                MkvToolnixCommandResult.Line(msg, type)
            } else null
        }.filterNotNull().asCachedSequence()
        return MkvToolnixCommandResult.Lazy(me(), reader, { p.waitFor() }, output)
    }

    protected abstract fun me() : SELF

    /**
     * Executes the command and waits for its completion.
     * The returned result object will have all the parsed data in place.
     *
     * In this case, contrary to [executeLazy], the input stream will be closed automatically.
     *
     * @return the sync result
     * @throws MkvToolnixCommandException when there is an error or warning. A specific class type will be thrown for each binary (i.e. [MkvToolnixCommandException.MkvPropEditException] and [MkvToolnixCommandException.MkvMergeException]
     */
    fun execute() = executeLazy().waitForCompletion(exceptionInitializer)

    /**
     * Executes the command while printing the output to the standard output. Useful for CLI applications.
     */
    fun executeAndPrint(printCommand: Boolean = false, printOutput: Boolean = true, printExitCode: Boolean = true) : MkvToolnixCommandResult.Sync<SELF> {
        return executeLazy().apply {
            print(printCommand, printOutput, printExitCode)
        }.toSync()
    }

    protected abstract val exceptionInitializer: ExceptionInitializer<SELF>

    companion object {
        private const val WARNING_PREFIX = "WARNING:"
        private const val ERROR_PREFIX = "ERROR:"
    }
}