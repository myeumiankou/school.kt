package com.school.kt.imagefilters.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.fragment.ListImageFragment
import org.jetbrains.anko.frameLayout

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout { id = R.id.container }
        supportFragmentManager.beginTransaction().add(R.id.container, ListImageFragment()).commit()
    }
}
