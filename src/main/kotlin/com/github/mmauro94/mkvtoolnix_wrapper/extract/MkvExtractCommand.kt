package com.github.mmauro94.mkvtoolnix_wrapper.extract

import com.github.mmauro94.mkvtoolnix_wrapper.*
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixCommandException.MkvExtractException
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixCommandException.MkvPropEditException
import java.io.File
import java.math.BigInteger

/**
 * Class to create a `mkvextract` command
 * @param sourceFile the file from where the tracks are to be extracted from
 */
class MkvExtractCommand internal constructor(
    val sourceFile: File
) : MkvToolnixCommand<MkvExtractCommand>(MkvToolnixBinary.MKV_EXTRACT) {

    //region GLOBAL OPTIONS
    val globalOptions = GlobalOptions()

    class GlobalOptions : CommandArgs {
        /** `--verbose`/`-v` option. Be verbose and show all the important Matroska™ elements as they're read. */
        var verbose: Boolean = false
        /** `--abort-on-warnings` option. Tells the program to abort after the first warning is emitted. The program's exit code will be 1. */
        var abortOnWarnings: Boolean = false

        val additionalArgs = AdditionalArgs()

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            if (verbose) {
                add("--verbose")
            }
            if (abortOnWarnings) {
                add("--abort-on-warnings")
            }
            add(additionalArgs)
        }
    }

    /**
     * @param f lambda that changes the global options
     */
    fun globalOptions(f: GlobalOptions.() -> Unit) = apply {
        f(globalOptions)
    }
    //endregion

    /** List of "extraction modes" that will be performed in the source file */
    val modes: MutableList<MkvExtractExtractionMode> = ArrayList()

    /**
     * Adds a new track extraction mode
     * @param f lambda that fills the provided [MkvExtractTrackExtractionMode] to set what tracks are to extract.
     */
    fun extractTracks(
        f: MkvExtractTrackExtractionMode.() -> Unit
    ): MkvExtractCommand {
        modes.add(MkvExtractTrackExtractionMode().apply(f))
        return this
    }

    /**
     * Adds a new attachment extraction mode
     * @param f lambda that fills the provided [MkvExtractAttachmentExtractionMode] to set what attachments are to extract.
     */
    fun extractAttachments(
        f: MkvExtractAttachmentExtractionMode.() -> Unit
    ): MkvExtractCommand {
        modes.add(MkvExtractAttachmentExtractionMode().apply(f))
        return this
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add(globalOptions)
        add(sourceFile.absolutePath.toString())
        modes.forEach { add(it) }
    }


    override val exceptionInitializer = ::MkvExtractException

    override fun me() = this
}
