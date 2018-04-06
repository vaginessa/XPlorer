package io.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class RxSharedPreferences private constructor(private val preferences: SharedPreferences) {
    private constructor(name: String, mode: Int = Context.MODE_PRIVATE) : this(baseContext.getSharedPreferences(name, mode))
    private constructor() : this(PreferenceManager.getDefaultSharedPreferences(baseContext))

    fun contains(key: String): Observable<Boolean> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.contains(key))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.contains(_key))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun contains(@StringRes keyResId: Int): Observable<Boolean> = contains(baseContext.getString(keyResId))

    fun getBoolean(key: String, defValue: Boolean = false): Observable<Boolean> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getBoolean(key, defValue))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getBoolean(_key, defValue))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getBoolean(@StringRes keyResId: Int, defValue: Boolean = false): Observable<Boolean> = getBoolean(baseContext.getString(keyResId), defValue)

    fun getInt(key: String, defValue: Int = 0): Observable<Int> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getInt(key, defValue))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getInt(_key, defValue))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getInt(@StringRes keyResId: Int, defValue: Int = 0): Observable<Int> = getInt(baseContext.getString(keyResId), defValue)

    fun getAll(): Observable<Map<String, *>> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.all)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            subject.onNext(sharedPreferences.all)
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun edit(): Editor = Editor()

    fun getLong(key: String, defValue: Long = 0L): Observable<Long> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getLong(key, defValue))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getLong(_key, defValue))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getLong(@StringRes keyResId: Int, defValue: Long = 0L): Observable<Long> = getLong(baseContext.getString(keyResId), defValue)

    fun getFloat(key: String, defValue: Float = 0f): Observable<Float> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getFloat(key, defValue))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getFloat(_key, defValue))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getFloat(@StringRes keyResId: Int, defValue: Float = 0f): Observable<Float> = getFloat(baseContext.getString(keyResId), defValue)

    fun getStringSet(key: String, defValues: Set<String> = emptySet()): Observable<Set<String>> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getStringSet(key, defValues))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getStringSet(_key, defValues))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getStringSet(@StringRes keyResId: Int, defValues: Set<String> = emptySet()): Observable<Set<String>> = getStringSet(baseContext.getString(keyResId), defValues)

    fun getString(key: String, defValue: String = ""): Observable<String> = Observable.defer {
        val subject = BehaviorSubject.createDefault(preferences.getString(key, defValue))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _key ->
            if (key == _key) subject.onNext(sharedPreferences.getString(_key, defValue))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        subject.doOnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun getString(@StringRes keyResId: Int, defValue: String = ""): Observable<String> = getString(baseContext.getString(keyResId), defValue)

    inner class Editor : SharedPreferences.Editor {
        private val editor = preferences.edit()
        override fun clear(): Editor = apply { editor.clear() }
        fun putLong(@StringRes keyResId: Int, value: Long): Editor = putLong(baseContext.getString(keyResId), value)
        override fun putLong(key: String, value: Long): Editor = apply { editor.putLong(key, value) }
        fun putInt(@StringRes keyResId: Int, value: Int): Editor = putInt(baseContext.getString(keyResId), value)
        override fun putInt(key: String, value: Int): Editor = apply { editor.putInt(key, value) }
        fun remove(@StringRes keyResId: Int): Editor = remove(baseContext.getString(keyResId))
        override fun remove(key: String): Editor = apply { editor.remove(key) }
        fun putBoolean(@StringRes keyResId: Int, value: Boolean): Editor = putBoolean(baseContext.getString(keyResId), value)
        override fun putBoolean(key: String, value: Boolean): Editor = apply { editor.putBoolean(key, value) }
        fun putStringSet(@StringRes keyResId: Int, values: Set<String>): Editor = putStringSet(baseContext.getString(keyResId), values)
        override fun putStringSet(key: String, values: Set<String>): Editor = apply { editor.putStringSet(key, values) }
        override fun commit(): Boolean = editor.commit()
        fun putFloat(@StringRes keyResId: Int, value: Float): Editor = putFloat(baseContext.getString(keyResId), value)
        override fun putFloat(key: String, value: Float): Editor = apply { editor.putFloat(key, value) }
        override fun apply() = editor.apply()
        fun putString(@StringRes keyResId: Int, value: String): Editor = putString(baseContext.getString(keyResId), value)
        override fun putString(key: String, value: String): Editor = apply { editor.putString(key, value) }
    }

    companion object {
        private val singletonMap = hashMapOf<String?, RxSharedPreferences>()
        fun getSingleton(): RxSharedPreferences = singletonMap.getOrPut(null) { RxSharedPreferences() }
        fun getSingleton(name: String, mode: Int = Context.MODE_PRIVATE): RxSharedPreferences = singletonMap.getOrPut("$name$mode") { RxSharedPreferences(name, mode) }
        fun create(preferences: SharedPreferences): RxSharedPreferences = RxSharedPreferences(preferences)
    }
}