package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.smarternships.R

class CompanyApplicantList : AppCompatActivity() {

    private lateinit var mTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.company_applicant_list)

        mTextView = findViewById<View>(R.id.applicant_feed_view) as TextView
        //TODO- populate scrollable view with all applicants
    }
}