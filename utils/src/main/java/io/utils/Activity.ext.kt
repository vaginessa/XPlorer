package io.utils

import android.app.Activity
import android.os.Parcelable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun stringExtra(key: String? = null, defValue: () -> String = { "" }): ReadWriteProperty<Activity, String> = object : ReadWriteProperty<Activity, String> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): String = thisRef.intent.getStringExtra(key
            ?: property.name) ?: defValue()

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: String) {
        thisRef.intent.putExtra(key ?: property.name, value)
    }
}

fun stringArrayExtra(key: String? = null, defValue: () -> Array<String> = { emptyArray() }): ReadWriteProperty<Activity, Array<String>> = object : ReadWriteProperty<Activity, Array<String>> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): Array<String> = thisRef.intent.getStringArrayExtra(key
            ?: property.name) ?: defValue()

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: Array<String>) {
        thisRef.intent.putExtra(key ?: property.name, value)
    }
}

inline fun <reified T : Parcelable?> parcelableExtra(key: String? = null, crossinline defValue: () -> T = { null as T }): ReadWriteProperty<Activity, T> = object : ReadWriteProperty<Activity, T> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): T = thisRef.intent.getParcelableExtra<T>(key
            ?: property.name) ?: defValue()

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        thisRef.intent.putExtra(key ?: property.name, value)
    }
}