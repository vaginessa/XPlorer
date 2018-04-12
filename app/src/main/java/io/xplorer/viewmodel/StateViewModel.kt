package io.xplorer.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class StateViewModel<out T> : ViewModel() {
    interface State
    inner class EmptyState : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.EmptyState
        override fun hashCode(): Int = javaClass.hashCode()
    }

    inner class ErrorState(val error: Throwable) : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.ErrorState && other.error == error
        override fun hashCode(): Int = error.hashCode()
    }

    inner class LoadingState : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.LoadingState
        override fun hashCode(): Int = javaClass.hashCode()
    }

    inner class ContentState(val content: T) : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.ContentState && other.content == content
        override fun hashCode(): Int = content?.hashCode() ?: 0
    }

    protected abstract fun provideState(): Observable<State>

    val state by lazy { provideState().distinctUntilChanged().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
}