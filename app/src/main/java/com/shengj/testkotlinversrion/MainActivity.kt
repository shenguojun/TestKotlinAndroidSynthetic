package com.shengj.testkotlinversrion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_main.text = "hello kotlin!"
    }

    fun startSyntheticListActivity(view: View) {
        val intent = Intent(this, SyntheticListActivity::class.java)
        startActivity(intent)
    }
}