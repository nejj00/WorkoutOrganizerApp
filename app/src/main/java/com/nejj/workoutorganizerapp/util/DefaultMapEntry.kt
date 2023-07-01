package com.nejj.workoutorganizerapp.util

class DefaultMapEntry<K, V>(
    private val defaultKey: K,
    private val defaultValue: V
) : Map.Entry<K, V> {
    override val key: K
        get() = defaultKey

    override val value: V
        get() = defaultValue
}