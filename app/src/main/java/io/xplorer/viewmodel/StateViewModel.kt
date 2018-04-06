package io.xplorer.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Observable

abstract class StateViewModel<out T>(app: Application) : AndroidViewModel(app) {
    abstract val state: Observable<State>

    interface State
    inner class LoadingState : State {
        override fun equals(other: Any?): Boolean = other?.hashCode() == hashCode()
        override fun hashCode(): Int = javaClass.hashCode()
    }

    inner class EmptyState : State {
        override fun equals(other: Any?): Boolean = other?.hashCode() == hashCode()
        override fun hashCode(): Int = javaClass.hashCode()
    }

    inner class ErrorState(
            val error: Throwable
    ) : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.ErrorState && other.error == error
        override fun hashCode(): Int = error.hashCode()
    }

    inner class ContentState(
            val content: T
    ) : State {
        override fun equals(other: Any?): Boolean = other is StateViewModel<*>.ContentState && other.content == content
        override fun hashCode(): Int = content?.hashCode() ?: ContentState::class.java.hashCode()
    }
}