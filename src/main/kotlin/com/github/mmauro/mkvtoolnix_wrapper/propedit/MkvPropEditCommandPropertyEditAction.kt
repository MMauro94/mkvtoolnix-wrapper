package com.github.mmauro.mkvtoolnix_wrapper.propedit

import com.github.mmauro.mkvtoolnix_wrapper.CommandArgs
import com.github.mmauro.mkvtoolnix_wrapper.MkvToolnixLanguage
import com.github.mmauro.mkvtoolnix_wrapper.MkvToolnixTrackSelector
import com.github.mmauro.mkvtoolnix_wrapper.add
import com.github.mmauro.mkvtoolnix_wrapper.propedit.MkvPropEditCommandPropertyEditAction.PropertyEdit.*
import com.github.mmauro.mkvtoolnix_wrapper.propedit.MkvPropEditCommandPropertyEditAction.PropertyEdit.Set

/**
 * Class that represents a list of [PropertyEdit]s for a particular selector.
 *
 * You can call the methods [add], [set] and [delete].
 *
 * For track selectors you can also call [setLanguage], [setUndeterminedLanguage], [setIsDefault], [setIsEnabled], [setName], [deleteName], [setOrDeleteName]
 *
 * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.description.edit)
 * @param T the tye of the selector
 * @param editSelector the selector of the thing to edit
 */
class MkvPropEditCommandPropertyEditAction<T : MkvPropEditPropertyEditSelector>(val editSelector: T) :
    MkvPropEditCommandAction {

    /**
     * Sealed class that represent a single action to a property.
     *
     * Implementations are:
     * * [Add]: to add a property
     * * [Set]: to set a property
     * * [Delete]: to delete a property
     * @param the name of the property you want to act on
     */
    sealed class PropertyEdit(val name: String) : CommandArgs {

        /**
         * Adds a property name with the value value. The property will be added even if such a property exists already.
         *
         * Note that most properties are unique and cannot occur more than once.
         *
         * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.description.add)
         * @param name the name of the property to add
         * @param value the value of the property to add
         */
        class Add(name: String, val value: String) : PropertyEdit(name) {
            override fun commandArgs() = listOf("--add", "$name=$value")
        }

        /**
         * Sets all occurrences of the property name to the value value. If no such property exists then it will be added.
         *
         * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.description.set)
         * @param name the name of the property to set
         * @param value the value of the property to set
         */

        class Set(name: String, val value: String) : PropertyEdit(name) {
            override fun commandArgs() = listOf("--set", "$name=$value")
        }

        /**
         * Deletes all occurrences of the property name. Note that some properties are required and cannot be deleted.
         *
         * See [mkvpropedit doc](https://mkvtoolnix.download/doc/mkvpropedit.html#mkvpropedit.description.delete)
         * @param name the name of the property to set
         */
        class Delete(name: String) : PropertyEdit(name) {
            override fun commandArgs() = listOf("--delete", name)
        }
    }

    /**
     * List of [PropertyEdit]s that will modify properties in the given selector ([MkvPropEditPropertyEditSelector])
     */
    val actions = ArrayList<PropertyEdit>()

    /**
     * See [Add] doc
     */
    fun add(name: String, value: String) = apply {
        actions.add(PropertyEdit.Add(name, value))
        return this
    }

    /**
     * See [Set] doc
     */
    fun set(name: String, value: String) = apply {
        actions.add(PropertyEdit.Set(name, value))
        return this
    }

    /**
     * See [Delete] doc
     */
    fun delete(name: String) = apply {
        actions.add(PropertyEdit.Delete(name))
        return this
    }

    override fun commandArgs(): List<String> = ArrayList<String>().apply {
        if (!actions.isEmpty()) {
            add("--edit")
            add(editSelector)
            actions.forEach { add(it) }
        }
    }
}

/**
 * Sets the language for the given track.
 * @param language the ISO 639-2 code of the language
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setLanguage(language: String) =
    set("language", language)

/**
 * Sets the language for the given track.
 * @param language the language
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setLanguage(language: MkvToolnixLanguage) =
    setLanguage(language.iso639_2)

/**
 * Sets the language for the given track to be `und` (undetermined)
 * @param language the language
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setUndeterminedLanguage() =
    setLanguage("und")

/**
 * Sets whether this track is the default track.
 * @param isDefault the `flag-default` value
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setIsDefault(isDefault: Boolean) =
    set("flag-default", if (isDefault) "1" else "0")

/**
 * Sets whether this track is an enabled.
 * @param isEnabled the `flag-enabled` value
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setIsEnabled(isEnabled: Boolean) =
    set("flag-enabled", if (isEnabled) "1" else "0")

/**
 * Set the name of the track.
 * @param name the name of the track
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setName(name: String) =
    set("name", name)

/**
 * Deletes the name of the track.
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.deleteName() = delete("name")

/**
 * Sets or delete the track name.
 * @param name the new name of the track, or `null` to delete.
 */
fun <T : MkvToolnixTrackSelector> MkvPropEditCommandPropertyEditAction<T>.setOrDeleteName(name: String?) =
    if (name == null) deleteName()
    else setName(name)
