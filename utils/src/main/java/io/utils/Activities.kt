package io.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

object Activities {
    fun start(intent: Intent, options: Bundle? = null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (options != null) baseContext.startActivity(intent, options)
        else baseContext.startActivity(intent)
    }

    fun startForResult(intent: Intent, options: Bundle? = null): Single<ActivityResult> {
        val key = UID.randomUid(8)
        val subject = SingleSubject.create<ActivityResult>()
        val i = Intent(baseContext, ResultActivity::class.java)
        ResultActivity.subjects[key] = subject
        i.putExtra("key", key)
        i.putExtra("launchIntent", intent)
        i.putExtra("options", options)
        start(i)
        return subject
    }

    data class ActivityResult(
            val resultCode: Int,
            val resultData: Intent
    )

    class ResultActivity : Activity() {
        private val key by stringExtra()
        private val launchIntent by parcelableExtra<Intent>()
        private val options by parcelableExtra<Bundle?>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            startActivityForResult(launchIntent, 0, options)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            finish()
            val subject = subjects.remove(key) ?: return
            subject.onSuccess(ActivityResult(resultCode, data ?: Intent()))
        }

        companion object {
            val subjects = hashMapOf<String, SingleSubject<ActivityResult>>()
        }
    }
}