package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.*
import com.github.mmauro.mkvtoolnix_wrapper.propedit.MkvPropEditCommandOperation.PropertyEdit
import java.io.File
import java.math.BigInteger

class MkvPropEditCommand(
    val file: File,
    val parseMode: MkvPropEditParseMode = MkvPropEditParseMode.FAST
) : CommandArgs {

    val operations = ArrayList<MkvPropEditCommandOperation>()
    var verbose = false
    var abortOnWarnings = false

    //region PROPERTY EDIT
    //region track
    fun editTrack(
        track: MkvToolnixTrack,
        f: PropertyEdit<MkvToolnixTrackSelector>.() -> Unit
    ): MkvPropEditCommand {
        operations.add(PropertyEdit(MkvToolnixTrackSelector.ofTrack(track)).apply(f))
        return this
    }

    fun editTrackUid(
        uid: BigInteger,
        f: PropertyEdit<MkvToolnixTrackSelector.TrackUidSelector>.() -> Unit
    ): MkvPropEditCommand {
        operations.add(PropertyEdit(MkvToolnixTrackSelector.TrackUidSelector(uid)).apply(f))
        return this
    }

    fun editTrackNumber(
        number: Int,
        f: PropertyEdit<MkvToolnixTrackSelector.TrackNumberSelector>.() -> Unit
    ): MkvPropEditCommand {
        operations.add(PropertyEdit(MkvToolnixTrackSelector.TrackNumberSelector(number)).apply(f))
        return this
    }

    fun editTrackPosition(
        position: Int,
        f: PropertyEdit<MkvToolnixTrackSelector.TrackPositionSelector>.() -> Unit
    ): MkvPropEditCommand {
        return editTrackPosition(position, null, f)
    }

    fun editTrackPosition(
        position: Int,
        trackType: MkvToolnixTrackType?,
        f: PropertyEdit<MkvToolnixTrackSelector.TrackPositionSelector>.() -> Unit
    ): MkvPropEditCommand {
        operations.add(PropertyEdit(MkvToolnixTrackSelector.TrackPositionSelector(position, trackType)).apply(f))
        return this
    }
    //endregion

    fun editSegmentInfo(f: PropertyEdit<SegmentInfoSelector>.() -> Unit) {
        operations.add(PropertyEdit(SegmentInfoSelector).apply(f))
    }
    //endregion

    //region ATTACHMENT
    //region add
    fun addAttachment(
        file: File,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Add.() -> Unit = {}
    ): MkvPropEditCommand {
        operations.add(MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Add(file).apply(f))
        return this
    }
    //endregion

    //region replace
    fun replaceAttachment(
        attachment: MkvToolnixAttachment,
        file: File,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace(
                MkvToolnixAttachmentSelector.ofAttachment(attachment),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentId(
        id: Long,
        file: File,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentUid(
        uid: BigInteger,
        file: File,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentName(
        name: String, file: File, f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name),
                file
            ).apply(f)
        )
    }

    fun replaceAttachmentMimeType(
        mimeType: String,
        file: File,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Replace(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType),
                file
            ).apply(f)
        )
    }
    //endregion

    //region update
    fun updateAttachment(
        attachment: MkvToolnixAttachment,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update.() -> Unit = {}
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update(
                MkvToolnixAttachmentSelector.ofAttachment(attachment)
            ).apply(f)
        )
    }

    fun updateAttachmentId(
        id: Long,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update.() -> Unit
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id)
            ).apply(f)
        )
    }

    fun updateAttachmentUid(
        uid: BigInteger,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update.() -> Unit
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid)
            ).apply(f)
        )
    }

    fun updateAttachmentName(
        name: String,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update.() -> Unit
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name)
            ).apply(f)
        )
    }

    fun updateAttachmentMimeType(
        mimeType: String,
        f: MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update.() -> Unit
    ) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.WithProperties.Update(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType)
            ).apply(f)
        )
    }
    //endregion

    //region delete
    fun deleteAttachment(attachment: MkvToolnixAttachment) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.Delete(
                MkvToolnixAttachmentSelector.ofAttachment(attachment)
            )
        )
    }

    fun deleteAttachmentId(id: Long) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.Delete(
                MkvToolnixAttachmentSelector.AttachmentIdSelector(id)
            )
        )
    }

    fun deleteAttachmentUid(uid: BigInteger) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.Delete(
                MkvToolnixAttachmentSelector.AttachmentUidSelector(uid)
            )
        )
    }

    fun deleteAttachmentName(name: String) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.Delete(
                MkvToolnixAttachmentSelector.AttachmentNameSelector(name)
            )
        )
    }

    fun deleteAttachmentMimeType(mimeType: String) = apply {
        operations.add(
            MkvPropEditCommandOperation.AttachmentEdit.Delete(
                MkvToolnixAttachmentSelector.AttachmentMimeTypeSelector(mimeType)
            )
        )
    }
    //endregion
    //endregion

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        if(verbose) {
            add("--verbose")
        }
        if(abortOnWarnings) {
            add("--abort-on-warnings")
        }
        add(parseMode)
        add(file.absolutePath.toString())
        operations.forEach { add(it) }
    }

    fun execute() {
        MkvToolnixBinary.MKV_PROP_EDIT.processBuilder(*commandArgs().toTypedArray()).start()
    }
}
