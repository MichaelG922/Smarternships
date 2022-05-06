package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.smarternships.R

class ViewJobActivity : AppCompatActivity() {

    private lateinit var mJobName: EditText
    private lateinit var mCompanyName: EditText
    private lateinit var mTimeFrame: EditText
    private lateinit var mIntern: EditText
    private lateinit var mJobDescription : EditText
    private lateinit var mViewIntern: Button
    private lateinit var mViewCompany: Button
    private lateinit var mApplyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_view_job)

        //TODO - grab the job information using DB
        mJobName = findViewById<View>(R.id.job_name) as EditText
        mIntern = findViewById<View>(R.id.intern_name) as EditText
        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.description) as EditText

        mViewIntern = findViewById<View>(R.id.view_intern) as Button
        mViewCompany = findViewById<View>(R.id.view_company) as Button
        mApplyButton = findViewById<View>(R.id.apply_button) as Button

        val i = intent
        val b = i.extras

        val isIntern = b?.getBoolean("ISINTERN")

        if(isIntern == false){
            mApplyButton.visibility = View.INVISIBLE
        }

        mViewCompany.setOnClickListener {
            //TODO - go to company account
        }

        mViewIntern.setOnClickListener {
            //todo - go to intern account
        }

        mApplyButton.setOnClickListener {
            //todo - applied to job
        }

    }
}