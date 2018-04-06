package io.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object Services {
    fun start(intent: Intent): ComponentName = baseContext.startService(intent)

    @RequiresApi(Build.VERSION_CODES.O)
    fun startForeground(intent: Intent): ComponentName = baseContext.startForegroundService(intent)

    inline fun <reified B : IBinder> bind(intent: Intent, flags: Int): Observable<ServiceBinding<B>> = Observable.defer {
        val subject = BehaviorSubject.create<ServiceBinding<B>>()
        val connection = object : android.content.ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder?) {
                subject.onNext(ServiceBinding(service as B, name))
            }

            override fun onServiceDisconnected(name: ComponentName) {
                subject.onNext(ServiceBinding(null, name))
            }

            override fun onBindingDied(name: ComponentName) {
                subject.onNext(ServiceBinding(null, name))
            }
        }
        baseContext.bindService(intent, connection, flags)
        subject.doOnDispose { baseContext.unbindService(connection) }
    }

    data class ServiceBinding<out T : IBinder>(
            val service: T?,
            val componentName: ComponentName
    )
}