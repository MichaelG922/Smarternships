package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.smarternships.R

class ViewJobCompanyActivity : AppCompatActivity() {

    private lateinit var mJobName: EditText
    private lateinit var mCompanyName: EditText
    private lateinit var mTimeFrame: EditText
    private lateinit var mIntern: EditText
    private lateinit var mJobDescription : EditText
    private lateinit var mViewIntern: Button
    private lateinit var mViewCompany: Button
    private lateinit var mApplicantsButton: Button
    private lateinit var mManageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.view_job_company)

        //TODO - grab the job information using DB
        mJobName = findViewById<View>(R.id.job_name) as EditText
        mIntern = findViewById<View>(R.id.intern_name) as EditText
        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.view_description) as EditText
        mViewIntern = findViewById<View>(R.id.view_intern) as Button
        mViewCompany = findViewById<View>(R.id.view_company) as Button
        mApplicantsButton = findViewById<View>(R.id.view_applicants) as Button
        mManageButton = findViewById<View>(R.id.manage_button) as Button

        mViewCompany.setOnClickListener {
            //TODO - go to company account
        }

        mViewIntern.setOnClickListener {
            //todo - go to intern account
        }

        mApplicantsButton.setOnClickListener {
            //todo - open applicants page
            //startIntent(Intent())
        }

        mManageButton.setOnClickListener {
            //todo - open manage job page
            //startIntent(Intent())
        }
    }
}