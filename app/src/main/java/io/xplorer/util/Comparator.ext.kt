package io.xplorer.util

fun <T> Comparator<T>.invert(reverse: Boolean): Comparator<T> =
        if (reverse) Comparator { o1, o2 -> -this@invert.compare(o1, o2) } else this