package com.school.kt.imagefilters.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.fragment.ListImageFragment

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container, ListImageFragment()).commit()
    }
}
