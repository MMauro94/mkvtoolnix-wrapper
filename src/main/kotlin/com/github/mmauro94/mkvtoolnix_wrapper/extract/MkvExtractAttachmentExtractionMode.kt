package com.github.mmauro94.mkvtoolnix_wrapper.extract

import com.github.mmauro94.mkvtoolnix_wrapper.*
import java.io.File

class MkvExtractAttachmentExtractionMode : MkvExtractExtractionMode {

    val tracks: MutableList<Pair<Long, File>> = ArrayList()

    fun addAttachment(attachment: MkvToolnixAttachment, file: File) {
        addAttachmentId(attachment.id, file)
    }

    fun addAttachmentId(attachmentId: Long, file: File) {
        tracks.add(attachmentId to file)
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add("attachments")
        tracks.forEach {(id,file)->
            add("$id:${file.absolutePath}")
        }
    }

}