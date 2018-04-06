package io.xplorer.fragment

import android.support.v4.app.Fragment

abstract class PageFragment<out T : Any> : Fragment() {
    private lateinit var _item: T
    val item: T get() = _item

    companion object {
        inline fun <T, reified F : PageFragment<T>> create(item: T): F {
            val fragment = F::class.java.newInstance()
            val field = PageFragment::class.java.getDeclaredField("_item")
            field.isAccessible = true
            field.set(fragment, item)
            return fragment
        }
    }
}