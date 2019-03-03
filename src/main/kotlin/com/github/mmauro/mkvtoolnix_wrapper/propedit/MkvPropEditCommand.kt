package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.*
import java.io.File

class MkvPropEditCommand(
    val file: File,
    val parseMode: ParseMode = ParseMode.FAST
) : CommandArgs {

    val edits = ArrayList<Edit<*>>()

    class Edit<T : EditSelector>(val editSelector: T) : CommandArgs {

        abstract class Action(val name: String) : CommandArgs {
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

        fun add(name: String, value: String): Edit<T> {
            actions.add(Action.Add(name, value))
            return this
        }

        fun set(name: String, value: String): Edit<T> {
            actions.add(Action.Set(name, value))
            return this
        }

        fun delete(name: String): Edit<T> {
            actions.add(Action.Delete(name))
            return this
        }

        override fun commandArgs(): List<String> = ArrayList<String>().apply {
            add(editSelector)
            actions.forEach { add(it) }
        }


        companion object {
            fun ofTrack(track: MkvToolnixTrack) =
                Edit(TrackSelector.ofTrack(track))
        }
    }

    fun addEdit(edit: Edit<*>): MkvPropEditCommand {
        edits.add(edit)
        return this
    }

    fun addTrackEdit(track: MkvToolnixTrack): Edit<TrackSelector> {
        return Edit.ofTrack(track).apply {
            addEdit(this)
        }
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        add(parseMode)
        add(file.absolutePath.toString())
        edits.forEach { add(it) }
    }
}

fun MkvPropEditCommand.Edit<out TrackSelector>.setLanguage(language: String) = set("language", language)

fun MkvPropEditCommand.Edit<out TrackSelector>.setLanguage(language: MkvToolnixLanguage) =
    setLanguage(language.iso639_2)

fun MkvPropEditCommand.Edit<out TrackSelector>.setIsDefault(isDefault: Boolean) =
    set("flag-default", if (isDefault) "1" else "0")


fun MkvPropEditCommand.Edit<out TrackSelector>.setIsEnabled(isEnabled: Boolean) =
    set("flag-enabled", if (isEnabled) "1" else "0")

fun MkvPropEditCommand.Edit<out TrackSelector>.setName(name: String) =
    set("name", name)

fun MkvPropEditCommand.Edit<out TrackSelector>.deleteName() = delete("name")