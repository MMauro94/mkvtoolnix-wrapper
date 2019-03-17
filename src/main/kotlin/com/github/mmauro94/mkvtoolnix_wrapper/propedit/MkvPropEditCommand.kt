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
 */
class MkvPropEditCommand(
    val sourceFile: File
) : MkvToolnixCommand<MkvPropEditCommand>(MkvToolnixBinary.MKV_PROP_EDIT) {

    object GlobalOptions : CommandArgs {
        /**
         * `--parse-mode` option. The parse mode. Defaults to [MkvPropEditParseMode.FAST]
         * @see MkvPropEditParseMode
         */
        val parseMode: MkvPropEditParseMode = MkvPropEditParseMode.FAST
        /** `--verbose`/`-v` option. Be verbose and show all the important Matroska™ elements as they're read. */
        var verbose: Boolean = false
        /** `--abort-on-warnings` option. Tells the program to abort after the first warning is emitted. The program's exit code will be 1. */
        var abortOnWarnings: Boolean = false

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            if (verbose) {
                add("--verbose")
            }
            if (abortOnWarnings) {
                add("--abort-on-warnings")
            }
            add(parseMode)
        }
    }

    /** List of actions that will be performed in the source file */
    val actions: MutableList<MkvPropEditCommandAction> = ArrayList()

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
    fun editTrackByPropertiesByPosition(
        position: Int,
        f: MkvPropEditCommandPropertyEditAction<MkvToolnixTrackSelector.PositionSelector>.() -> Unit
    ): MkvPropEditCommand {
        return editTrackByPropertiesByPosition(position, null, f)
    }

    /**
     * Edit a track by its position in the source file (of the given track types)
     * @param position the 1-index position of the track in the source file
     * @param trackType the track type
     * @see editTrackProperties
     * @see MkvToolnixTrackSelector.PositionSelector
     */
    fun editTrackByPropertiesByPosition(
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
     * @see SegmentInfoSelector
     */
    fun editSegmentInfo(f: MkvPropEditCommandPropertyEditAction<SegmentInfoSelector>.() -> Unit) {
        actions.add(MkvPropEditCommandPropertyEditAction(SegmentInfoSelector).apply(f))
    }
    //endregion

    //region ATTACHMENT
    //region add
    /**
     * Adds an attachment.
     *
     * ```kotlin
     * MkvPropEditCommand(File("..."))
     *  .addAttachment(File("poster.png")) { //<- lambda to set object properties
     *      name = "main poster"
     *      description = "the movie poster"
     *      mimeType = "image/png"
     *  }
     * ```
     * @param file the attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     */
    fun addAttachment(
        file: File,
        f: MkvPropEditCommandAttachmentEditAction.WithProperties.Add.() -> Unit = {}
    ): MkvPropEditCommand {
        actions.add(MkvPropEditCommandAttachmentEditAction.WithProperties.Add(file).apply(f))
        return this
    }
    //endregion

    //region replace
    /**
     * Replace an already existing attachment.
     *
     * ```kotlin
     * MkvPropEditCommand(File("..."))
     *  .replaceAttachment(attachment, File("new attachment.png")) { //<- lambda to set object properties
     *      name = "new name"
     *  }
     * ```
     * @param attachment the attachment to replace
     * @param file the new attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see MkvToolnixAttachmentSelector.ofAttachment
     */
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

    /**
     * Replace an already existing attachment by its id.
     * @param id the id of the attachment to replace
     * @param file the new attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see replaceAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentIdSelector
     */
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

    /**
     * Replace an already existing attachment by its UID.
     * @param uid the UID of the attachment to replace
     * @param file the new attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see replaceAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentUidSelector
     */
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

    /**
     * Replace an already existing attachment by its name.
     * @param name the name of the attachment to replace
     * @param file the new attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see replaceAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentNameSelector
     */
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

    /**
     * Replace an already existing attachment by its mime type.
     * @param mimeType the mime type of the attachment to replace
     * @param file the new attachment
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see replaceAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector
     */
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
    /**
     * Updates an already existing attachment.
     *
     * ```kotlin
     * MkvPropEditCommand(File("..."))
     *  .updateAttachment(attachment) { //<- lambda to set object properties
     *      name = "new name"
     *      description = "new description"
     *  }
     * ```
     * @param attachment the attachment to replace
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see MkvToolnixAttachmentSelector.ofAttachment
     */
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

    /**
     * Updates an already existing attachment by its id.

     * @param id the id of the attachment to replace
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see updateAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentIdSelector
     */
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

    /**
     * Updates an already existing attachment by its UID.

     * @param uid the UID of the attachment to replace
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see updateAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentUidSelector
     */
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

    /**
     * Updates an already existing attachment by its name.

     * @param name the name of the attachment to replace
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see updateAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentNameSelector
     */
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

    /**
     * Updates an already existing attachment by its mime type.

     * @param mimeType the mime type of the attachment to replace
     * @param f lambda that fills the provided [MkvPropEditCommandAttachmentEditAction] to set the attachment info.
     * @see updateAttachment
     * @see MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector
     */
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
    /**
     * Deletes an attachment.
     * @param attachment the attachment to delete
     * @see MkvToolnixAttachmentSelector.ofAttachment
     */
    fun deleteAttachment(attachment: MkvToolnixAttachment) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.ofAttachment(attachment)
            )
        )
    }

    /**
     * Deletes an attachment by its id.
     * @param id the id of the attachment to delete.
     * @see MkvToolnixAttachmentSelector.AttachmentIdSelector
     */
    fun deleteAttachmentById(id: Long) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id)
            )
        )
    }

    /**
     * Deletes an attachment by its UID.
     * @param uid the UID of the attachment to delete.
     * @see MkvToolnixAttachmentSelector.AttachmentUidSelector
     */
    fun deleteAttachmentByUid(uid: BigInteger) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid)
            )
        )
    }

    /**
     * Deletes an attachment by its name.
     * @param name the name of the attachment to delete.
     * @see MkvToolnixAttachmentSelector.AttachmentNameSelector
     */
    fun deleteAttachmentByName(name: String) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name)
            )
        )
    }

    /**
     * Deletes an attachment by its mime type.
     * @param mimeType the mime type of the attachment to delete.
     * @see MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector
     */
    fun deleteAttachmentByMimeType(mimeType: String) = apply {
        actions.add(
            MkvPropEditCommandAttachmentEditAction.Delete(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType)
            )
        )
    }
    //endregion
    //endregion

    /**
     * @param f lambda that changes the global options
     */
    fun globalOptions(f: GlobalOptions.() -> Unit) = apply {
        f(GlobalOptions)
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add(GlobalOptions)
        add(sourceFile.absolutePath.toString())
        actions.forEach { add(it) }
    }



    override val exceptionInitializer = ::MkvPropEditException

    override fun me()= this
}
