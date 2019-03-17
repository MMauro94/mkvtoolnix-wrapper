package com.github.mmauro94.mkvtoolnix_wrapper

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
     * Starts the execution of the command, returning the result object immediately, lazingly parsing the output.
     * The output can be iterated while the program is running. Calling [MkvToolnixCommandResult.exitCode] will obviously halt until the command terminates.
     *
     * WARNING! If you call this method, you will be responsible for closing the input stream. You can do so by calling [MkvToolnixCommandResult.Lazy.close] on the returned object. It is therefore suggested to [use] the returned object immediately
     *
     * @return the lazy result
     */
    abstract fun executeLazy(): MkvToolnixCommandResult.Lazy<SELF>

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
}