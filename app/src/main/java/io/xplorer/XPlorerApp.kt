package io.xplorer

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

class XPlorerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        modelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    }

    companion object {
        private lateinit var modelFactory: ViewModelProvider.AndroidViewModelFactory

        fun <M : ViewModel> createViewModel(modelClass: KClass<M>): M = modelFactory.create(modelClass.java)
    }
}