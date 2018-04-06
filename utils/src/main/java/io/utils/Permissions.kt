package io.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

object Permissions {
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun request(vararg permissions: String): Single<PermissionsResult> {
        val granted = hashSetOf<String>()
        val denied = hashSetOf<String>()
        permissions.forEach {
            if (isPermissionGranted(it)) granted += it
            else denied += it
        }
        if (denied.isEmpty()) return Single.just(PermissionsResult(granted, denied))
        val key = UID.randomUid(8)
        val subject = SingleSubject.create<PermissionsResult>()
        PermissionsActivity.singles[key] = subject
        Activities.start(intentFor<PermissionsActivity> {
            putExtra("key", key)
            putExtra("granted", granted.toTypedArray())
            putExtra("denied", denied.toTypedArray())
        })
        return subject
    }

    class PermissionsActivity : Activity() {
        private val granted by stringArrayExtra()
        private val denied by stringArrayExtra()
        private val key by stringExtra()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            ActivityCompat.requestPermissions(this, denied, 0)
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            finish()
            val subject = singles.remove(key) ?: return
            val granted = hashSetOf<String>()
            val denied = hashSetOf<String>()
            permissions.forEachIndexed { index, s ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) granted += s
                else denied += s
            }
            subject.onSuccess(PermissionsResult((this.granted + granted).toSet(), denied.toSet()))
        }

        companion object {
            val singles = hashMapOf<String, SingleSubject<PermissionsResult>>()
        }
    }

    data class PermissionsResult(
            val granted: Set<String>,
            val denied: Set<String>
    )
}