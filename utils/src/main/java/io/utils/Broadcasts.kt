package io.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object Broadcasts {
    fun send(data: Intent) {
        baseContext.sendBroadcast(data)
    }

    fun listen(vararg actions: String): Observable<Intent> {
        val filter = IntentFilter()
        actions.forEach { filter.addAction(it) }
        return listen(filter)
    }

    fun listen(filter: IntentFilter): Observable<Intent> = Observable.defer {
        val subject = BehaviorSubject.create<Intent>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (filter.hasAction(intent?.action)) subject.onNext(intent ?: Intent())
            }
        }
        baseContext.registerReceiver(receiver, filter)
        subject.doOnDispose { baseContext.unregisterReceiver(receiver) }
    }
}