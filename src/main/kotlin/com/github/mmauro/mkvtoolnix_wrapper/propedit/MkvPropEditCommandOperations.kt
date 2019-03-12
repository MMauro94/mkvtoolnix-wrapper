package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.*
import java.io.File
import java.math.BigInteger

sealed class MkvPropEditCommandOperation : CommandArgs {

    class PropertyEdit<T : MkvPropEditPropertyEditSelector>(val editSelector: T) : MkvPropEditCommandOperation() {

        sealed class Action(val name: String) : CommandArgs {
            class Add(name: String, val value: String) : Action(name) {
                override fun commandArgs() = listOf("--add", "$name=$value")
            }

            class Set(name: String, val value: String) : Action(name) {
                override fun commandArgs() = listOf("--set", "$name=$value")
            }

            class Delete(name: String) : Action(name) {
                override fun commandArgs() = listOf("--delete", name)
            }
        }

        val actions = ArrayList<Action>()

        fun add(name: String, value: String): PropertyEdit<T> {
            actions.add(Action.Add(name, value))
            return this
        }

        fun set(name: String, value: String): PropertyEdit<T> {
            actions.add(Action.Set(name, value))
            return this
        }

        fun delete(name: String): PropertyEdit<T> {
            actions.add(Action.Delete(name))
            return this
        }

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            add("--edit")
            add(editSelector)
            actions.forEach { add(it) }
        }
    }

    sealed class AttachmentEdit : MkvPropEditCommandOperation() {

        sealed class WithProperties : AttachmentEdit() {

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
                    add("attachment-uid")
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

        class Delete(val selector: MkvToolnixAttachmentSelector) : AttachmentEdit() {
            override fun commandArgs() = listOf("--delete-attachment", selector.commandArg())
        }

    }
}

fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.setLanguage(language: String) =
    set("language", language)

fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.setLanguage(language: MkvToolnixLanguage) =
    setLanguage(language.iso639_2)

fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.setIsDefault(isDefault: Boolean) =
    set("flag-default", if (isDefault) "1" else "0")


fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.setIsEnabled(isEnabled: Boolean) =
    set("flag-enabled", if (isEnabled) "1" else "0")

fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.setName(name: String) =
    set("name", name)

fun MkvPropEditCommandOperation.PropertyEdit<out MkvToolnixTrackSelector>.deleteName() = delete("name")