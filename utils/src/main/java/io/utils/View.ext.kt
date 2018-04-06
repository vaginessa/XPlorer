package io.utils

import android.support.v4.view.ViewCompat
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

fun <T, V : View> V.bind(observable: Observable<T>, onNext: V.(T) -> Unit, onError: V.(Throwable) -> Unit) {
    bind(observable, onNext, onError) {}
}

fun <T, V : View> V.bind(observable: Observable<T>, onNext: V.(T) -> Unit) {
    bind(observable, onNext) { throw it }
}

fun <T, V : View> V.bind(observable: Observable<T>, onNext: V.(T) -> Unit, onError: V.(Throwable) -> Unit, onComplete: V.() -> Unit) {
    val listener = object : View.OnAttachStateChangeListener {
        private var disposable: Disposable? = null
        override fun onViewDetachedFromWindow(v: View?) {
            disposable?.dispose()
            disposable = null
        }

        override fun onViewAttachedToWindow(v: View?) {
            disposable?.dispose()
            disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { onNext(it) },
                    { onError(it) },
                    { onComplete() }
            )
        }
    }
    addOnAttachStateChangeListener(listener)
    if (ViewCompat.isAttachedToWindow(this)) listener.onViewAttachedToWindow(this)
    else listener.onViewDetachedFromWindow(this)
}