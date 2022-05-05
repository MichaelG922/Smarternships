package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.smarternships.R

class ViewJobActivity : AppCompatActivity() {

    private lateinit var mJobName: EditText
    private lateinit var mCompanyName: EditText
    private lateinit var mTimeFrame: EditText
    private lateinit var mJobDescription : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_view_job)

        //TODO - grab the job information using DB
        mJobName = findViewById<View>(R.id.job_name) as EditText

        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.job_description) as EditText

    }
}