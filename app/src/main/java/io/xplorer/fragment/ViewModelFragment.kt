package io.xplorer.fragment

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import io.xplorer.XPlorerApp
import kotlin.reflect.KClass

abstract class ViewModelFragment<out M : ViewModel>(modelClass: KClass<M>) : Fragment() {
    val model by lazy { XPlorerApp.createViewModel(modelClass) }
}