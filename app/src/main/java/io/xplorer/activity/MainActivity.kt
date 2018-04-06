package io.xplorer.activity

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import io.xplorer.fragment.DirectoryFragment
import io.xplorer.fragment.PageFragment
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = PageFragment.create<File, DirectoryFragment>(Environment.getExternalStorageDirectory())
            supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
        }
    }
}
