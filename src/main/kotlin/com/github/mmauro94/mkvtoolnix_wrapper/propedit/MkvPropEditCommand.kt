package com.github.mmauro94.mkvtoolnix_wrapper.propedit

import com.github.mmauro94.mkvtoolnix_wrapper.*
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixCommandException.MkvPropEditException
import com.github.mmauro94.mkvtoolnix_wrapper.utils.asCachedSequence
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.math.BigInteger

/**
 * Class to create a `mkvpropedit` command
 * @param sourceFile the file that needs to be modified
 * @param parseMode the parse mode. Defaults to [MkvPropEditParseMode.FAST]
 * @param verbose `--verbose`/`-v` option. Be verbose and show all the important Matroska™ elements as they're read.
 * @param abortOnWarnings `--abort-on-warnings` option. Tells the program to abort after the first warning is emitted. The program's exit code will be 1.
 */
class MkvPropEditCommand(
    val sourceFile: File,
    val parseMode: MkvPropEditParseMode = MkvPropEditParseMode.FAST,
    var verbose : Boolean = false,
    var abortOnWarnings : Boolean = false
) : MkvToolnixCommand<MkvPropEditCommand>(MkvToolnixBinary.MKV_PROP_EDIT) {

    /** List of actions that will be performed in the source file */
    val actions = ArrayList<MkvPropEditCommandAction>()

    //region PROPERTY EDIT
    //region track

    /**
     * Edit the given track. Example:
     * ```kotlin
     * MkvPropEditCommand(File("..."))
     *  .editTrackProperties(track) { //<- lambda to set object properties
     *      set("prop-name1", "hello")
     *      delete("prop-name1")
     *      add("prop-name3", "world")
     *  }
     * ```
     *
     * @param track the track to edit
     * @param f lambda that fills the provided [MkvPropEditCommandPropertyEditAction] to set what properties to edit.
     * @see MkvToolnixTrackSelector.ofTrack
     */
    fun editTrackProperties(
        track: MkvToolnixTrack,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector>.() -> Unit
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandPropertyEditAction(MkvToolnixTrackSelector.ofTrack(track)).apply(f))
        return this
    }

    /**
     * Edit a track by its UID.
     * @param uid the track UID
     * @see editTrackProperties
     * @see MkvToolnixTrackSelector.UidSelector
     */
    fun editTrackPropertiesByUid(
        uid: BigInteger,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector.UidSelector>.() -> Unit
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandPropertyEditAction(MkvToolnixTrackSelector.UidSelector(uid)).apply(f))
        return this
    }

    /**
     * Edit a track by its number.
     * @param number the track number
     * @see editTrackProperties
     * @see MkvToolnixTrackSelector.NumberSelector
     */
    fun editTrackPropertiesByNumber(
        number: Int,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector.NumberSelector>.() -> Unit
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandPropertyEditAction(MkvToolnixTrackSelector.NumberSelector(number)).apply(f))
        return this
    }

    /**
     * Edit a track by its position in the source file.
     * @param position the 1-index position of the track in the source file
     * @see editTrackProperties
     * @see MkvToolnixTrackSelector.PositionSelector
     */
    fun editTrackByPropertiesPosition(
        position: Int,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector.PositionSelector>.() -> Unit
    ): MkvPropEditCommand {
        return editTrackByPropertiesPosition(position, null, f)
    }

    /**
     * Edit a track by its position in the source file (of the given track types)
     * @param position the 1-index position of the track in the source file
     * @param trackType the track type
     * @see editTrackProperties
     * @see MkvToolnixTrackSelector.PositionSelector
     */
    fun editTrackByPropertiesPosition(
        position: Int,
        trackType: MkvToolnixTrackType?,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector.PositionSelector>.() -> Unit
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandPropertyEditAction(MkvToolnixTrackSelector.PositionSelector(position, trackType)).apply(f))
        return this
    }
    //endregion

    /**
     * Edit the properties for the segment info.
     * The lambda parameter works the same as in [editTrackProperties]
     * @param f lambda that fills the provided [MkvPropEditCommandPropertyEditAction] to set what properties to edit.
     */
    fun editSegmentInfo(f: MkvPropEditCommandPropertyEditAction<SegmentInfoSelector>.() -> Unit) {
        actions.add(MkvPropEditCommandPropertyEditAction(SegmentInfoSelector).apply(f))
    }
    //endregion

    //region ATTACHMENT
    //region add
    fun addAttachment(
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Add.() -> Unit = {}
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandAttachmentEditAction.WithProperties.Add(file).apply(f))
        return this
    }
    //endregion

    //region replace
    fun replaceAttachment(
        attachment: MkvToolnixAttachment,
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Replace(
                MkvToolnixAttachmentSelector.ofAttachment(attachment),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentById(
        id: Long,
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentByUid(
        uid: BigInteger,
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentByName(
        name: String, file: File, f: MkvPropEditCommandAttachmentEditAction.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentByMimeType(
        mimeType: String,
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType),
                file
            ).apply(f)
        )
    }
    //endregion

    //region update
    fun updateAttachment(
        attachment: MkvToolnixAttachment,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Update.() -> Unit = {}
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Update(
                MkvToolnixAttachmentSelector.ofAttachment(attachment)
            ).apply(f)
        )
    }

    fun updateAttachmentById(
        id: Long,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Update.() -> Unit
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id)
            ).apply(f)
        )
    }

    fun updateAttachmentByUid(
        uid: BigInteger,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Update.() -> Unit
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid)
            ).apply(f)
        )
    }

    fun updateAttachmentByName(
        name: String,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Update.() -> Unit
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name)
            ).apply(f)
        )
    }

    fun updateAttachmentByMimeType(
        mimeType: String,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Update.() -> Unit
    ) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType)
            ).apply(f)
        )
    }
    //endregion

    //region delete
    fun deleteAttachment(attachment: MkvToolnixAttachment) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.ofAttachment(attachment)
            )
        )
    }

    fun deleteAttachmentById(id: Long) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id)
            )
        )
    }

    fun deleteAttachmentByUid(uid: BigInteger) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid)
            )
        )
    }

    fun deleteAttachmentByName(name: String) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name)
            )
        )
    }

    fun deleteAttachmentByMimeType(mimeType: String) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType)
            )
        )
    }
    //endregion
    //endregion

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        if (verbose) {
            add("--verbose")
        }
        if (abortOnWarnings) {
            add("--abort-on-warnings")
        }
        add(parseMode)
        add(sourceFile.absolutePath.toString())
        actions.forEach { add(it) }
    }

    companion object {
        private const val WARNING_PREFIX = "WARNING:"
        private const val ERROR_PREFIX = "ERROR:"
    }

    override val exceptionInitializer = ::MkvPropEditException

    override fun executeLazy(): MkvToolnixCommandResult.Lazy<MkvPropEditCommand> {
        val p = processBuilder().apply {
            redirectErrorStream(true)
        }.start()

        val reader = BufferedReader(InputStreamReader(p.inputStream))
        val output = reader.lineSequence().map { line ->
            Thread.sleep(500)
            val (msg, type) = if (line.startsWith(WARNING_PREFIX)) {
                line.substring(WARNING_PREFIX.length).trimStart() to MkvToolnixCommandResult.Line.Type.WARNING
            } else if (line.startsWith(ERROR_PREFIX)) {
                line.substring(ERROR_PREFIX.length).trimStart() to MkvToolnixCommandResult.Line.Type.ERROR
            } else {
                line to MkvToolnixCommandResult.Line.Type.INFO
            }
            if (type != MkvToolnixCommandResult.Line.Type.INFO || msg.isNotBlank()) {
                MkvToolnixCommandResult.Line(msg, type)
            } else null
        }.filterNotNull().asCachedSequence()

        return MkvToolnixCommandResult.Lazy(this, reader, { p.waitFor() }, output)
    }
}
