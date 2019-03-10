package com.github.mmauro.mkvtoolnix_wrapper

import java.math.BigInteger

sealed class MkvToolnixAttachmentSelector : CommandArg {

    class AttachmentIdSelector(val id : Long) : MkvToolnixAttachmentSelector() {
        override fun commandArg() = id.toString()
    }

    class AttachmentUidSelector(val uid : BigInteger) : MkvToolnixAttachmentSelector() {
        override fun commandArg() = "=$uid"
    }

    class AttachmentNameSelector(val name : String) : MkvToolnixAttachmentSelector() {
        override fun commandArg() = "name:${name.mkvtoolnixEscape()}"
    }

    class AttachmentMimeTypeSelector(val mimeType : String) : MkvToolnixAttachmentSelector() {
        override fun commandArg() = "mime-type:${mimeType.mkvtoolnixEscape()}"
    }

    companion object {
        @JvmStatic
        fun ofAttachment(attachment: MkvToolnixAttachment) = MkvToolnixAttachmentSelector.AttachmentUidSelector(attachment.properties.uid);
    }
}