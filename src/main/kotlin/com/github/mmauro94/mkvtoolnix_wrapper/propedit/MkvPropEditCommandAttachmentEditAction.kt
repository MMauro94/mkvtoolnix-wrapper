package com.github.mmauro94.mkvtoolnix_wrapper.propedit

import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixAttachmentSelector
import java.io.File
import java.math.BigInteger

sealed class MkvPropEditCommandAttachmentEditAction : MkvPropEditCommandAction {

    sealed class WithProperties : MkvPropEditCommandAttachmentEditAction() {

        var name: String? = null
        var mimeType: String? = null
        var description: String? = null
        var uid: BigInteger? = null

        protected fun propertiesCommandArgs() = ArrayList<String>().apply {
            name?.let {
                add("--attachment-name")
                add(it)
            }
            mimeType?.let {
                add("--attachment-mime-type")
                add(it)
            }
            description?.let {
                add("--attachment-description")
                add(it)
            }
            uid?.let {
                add("--attachment-uid")
                add(it.toString())
            }
        }

        class Add(val file: File) : WithProperties() {
            override fun commandArgs(): List<String> = propertiesCommandArgs().apply {
                add("--add-attachment")
                add(file.absolutePath.toString())
            }
        }

        class Replace(val selector: MkvToolnixAttachmentSelector, val file: File) : WithProperties() {
            override fun commandArgs(): List<String> = propertiesCommandArgs().apply {
                add("--replace-attachment")
                add(selector.commandArg() + ":" + file.absolutePath.toString())
            }
        }

        class Update(val selector: MkvToolnixAttachmentSelector) : WithProperties() {
            override fun commandArgs() = propertiesCommandArgs().apply {
                add("--update-attachment")
                add(selector.commandArg())
            }
        }
    }

    class Delete(val selector: MkvToolnixAttachmentSelector) : MkvPropEditCommandAttachmentEditAction() {
        override fun commandArgs() = listOf("--delete-attachment", selector.commandArg())
    }

}