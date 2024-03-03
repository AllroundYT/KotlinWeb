package de.allround.kotlinweb.util

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.BiConsumer

class MultiMap<I, T>(val map: ConcurrentMap<I, MutableList<T>> = ConcurrentHashMap()) {


    private var disableDuplication = true

    constructor(vararg pairs: Pair<I, T>) : this() {
        pairs.forEach { pair ->
            add(pair.first, pair.second)
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        map.forEach { (i: I, ts: List<T>) ->
            stringBuilder.append("\"${i.toString()}\" - [${ts.joinToString { ", " }}]${System.lineSeparator()}")
        }
        return stringBuilder.toString()
    }

    fun remove(i: I, t: T) {
        map.getOrDefault(i, mutableListOf()).remove(t)
    }

    fun remove(i: I) {
        map.remove(i)
    }

    val isEmpty: Boolean
        get() = map.isEmpty()


    fun add(vararg pair: Pair<I, T>) {
        pair.forEach {
            add(it.first, it.second)
        }
    }
    fun add(index: I, vararg objects: T) {
        map.compute(index) { _: I, list: MutableList<T>? ->
            var ts = list
            if (ts == null) ts = ArrayList()
            for (o in objects) {
                if (disableDuplication) {
                    map.forEach { (_: I, ts1: MutableList<T>) -> ts1.remove(o) }
                }
                if (!ts.contains(o)) {
                    ts.add(o)
                }
            }
            ts
        }
    }

    val keyWithMostEntries: I?
        get() {
            if (map.isEmpty()) return null
            return map.maxBy { entry -> entry.value.size }.key
        }

    fun forEach(consumer: BiConsumer<I, MutableList<T>>?): MultiMap<I, T> {
        map.forEach(consumer!!)
        return this
    }

    fun get(index: I): List<T> {
        return map.getOrDefault(index, emptyList())
    }
}
