package com.github.mmauro.mkvtoolnix_wrapper.utils

internal class CachedSequence<T>(sequence: Sequence<T>) : Sequence<T> {

    private val list = ArrayList<T>()
    private val iterator = sequence.iterator()

    override fun iterator() = object : Iterator<T> {
        var i = 0
        override fun hasNext(): Boolean {
            synchronized(iterator) {
                return if (i < list.size) {
                    true
                } else {
                    iterator.hasNext()
                }
            }
        }

        override fun next(): T {
            synchronized(iterator) {
                return if (i < list.size) {
                    list[i]
                } else {
                    iterator.next().also {
                        list.add(it)
                    }
                }.also { i++ }
            }
        }
    }
}

internal fun <T> Sequence<T>.asCachedSequence() = CachedSequence(this)