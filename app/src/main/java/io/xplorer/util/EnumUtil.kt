package io.xplorer.util

object EnumUtil {
    inline fun <reified T : Enum<T>> valueOf(name: String, defaultValue: T): T = try {
        java.lang.Enum.valueOf(T::class.java, name)
    } catch (_: IllegalArgumentException) {
        defaultValue
    }
}