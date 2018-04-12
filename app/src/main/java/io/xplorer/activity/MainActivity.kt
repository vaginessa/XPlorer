package io.xplorer.activity

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import io.xplorer.fragment.DirectoryFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = DirectoryFragment()
        fragment.open(Environment.getExternalStorageDirectory())
        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
    }

}
