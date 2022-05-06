package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.smarternships.R

class ViewJobsInternList : AppCompatActivity() {

    private lateinit var mTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.view_jobs_intern)

        mTextView = findViewById<View>(R.id.feed_view) as TextView
        //TODO- populate scrollable view with all jobs
    }
}