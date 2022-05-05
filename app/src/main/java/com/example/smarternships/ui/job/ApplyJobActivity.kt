package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R

class ApplyJobActivity : AppCompatActivity() {

    private lateinit var mJobName: EditText
    private lateinit var mCompanyName: EditText
    private lateinit var mTimeFrame: EditText
    private lateinit var mJobDescription : EditText
    private lateinit var mApplyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apply_job)
        supportActionBar?.hide()

        //TODO - populate job info from DB
        // button operation -> (should go back to prev activity)
        mJobName = findViewById<View>(R.id.job_name) as EditText
        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.description) as EditText
        mApplyButton = findViewById<View>(R.id.apply_button) as Button

        mApplyButton.setOnClickListener {
            Toast.makeText(applicationContext, "Applied to ${mJobName.text}!", Toast.LENGTH_SHORT).show()
            //submit to database
            setResult(RESULT_OK)
        }
    }
}