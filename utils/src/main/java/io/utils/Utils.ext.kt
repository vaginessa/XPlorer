package io.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.widget.Toast

inline fun bundle(init: Bundle.() -> Unit): Bundle {
    val bundle = Bundle()
    bundle.init()
    return bundle
}

inline fun bundle(loader: ClassLoader, init: Bundle.() -> Unit): Bundle {
    val bundle = Bundle(loader)
    bundle.init()
    return bundle
}

inline fun bundle(capacity: Int, init: Bundle.() -> Unit): Bundle {
    val bundle = Bundle(capacity)
    bundle.init()
    return bundle
}

inline fun bundle(b: Bundle, init: Bundle.() -> Unit): Bundle {
    val bundle = Bundle(b)
    bundle.init()
    return bundle
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
inline fun bundle(b: PersistableBundle, init: Bundle.() -> Unit): Bundle {
    val bundle = Bundle(b)
    bundle.init()
    return bundle
}

inline fun intent(init: Intent.() -> Unit): Intent {
    val intent = Intent()
    intent.init()
    return intent
}

inline fun intent(action: String, init: Intent.() -> Unit): Intent {
    val intent = Intent(action)
    intent.init()
    return intent
}

inline fun intent(action: String, uri: Uri, init: Intent.() -> Unit): Intent {
    val intent = Intent(action, uri)
    intent.init()
    return intent
}

inline fun <reified T> intentFor(init: Intent.() -> Unit): Intent {
    val intent = Intent(baseContext, T::class.java)
    intent.init()
    return intent
}

inline fun <reified T> intentFor(action: String, uri: Uri, init: Intent.() -> Unit): Intent {
    val intent = Intent(action, uri, baseContext, T::class.java)
    intent.init()
    return intent
}

fun toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG): Toast {
    val toast = Toast.makeText(baseContext, text, duration)
    toast.show()
    return toast
}

fun toast(@StringRes textResId: Int, duration: Int = Toast.LENGTH_LONG): Toast {
    val toast = Toast.makeText(baseContext, textResId, duration)
    toast.show()
    return toast
}