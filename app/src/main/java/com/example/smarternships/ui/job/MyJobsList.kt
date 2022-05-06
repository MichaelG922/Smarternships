package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.smarternships.R

class MyJobsList : AppCompatActivity() {

    private lateinit var mTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.my_jobs_list)

        mTextView = findViewById<View>(R.id.job_feed_view) as TextView
        val i = intent
        val b = i.extras

        val isIntern = b?.getBoolean("ISINTERN")
        //todo - populate job list based on if intern or company (buttons?)
    }
}