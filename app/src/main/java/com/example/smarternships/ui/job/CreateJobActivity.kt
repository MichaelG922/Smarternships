package com.example.smarternships.ui.job

import android.widget.Button

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.databinding.ActivityRegisterBinding

class CreateJobActivity: AppCompatActivity() {
    private lateinit var mCreateJobButton: Button
    private lateinit var mJobNameField : EditText
    private lateinit var mJobTimeFrameField : EditText
    private lateinit var mJobDescriptionField : EditText
    override fun onCreate(savedInstanceState: Bundle?){
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_job)

        mCreateJobButton = findViewById<View>(R.id.createjob) as Button
        mJobNameField = findViewById<View>(R.id.jobname) as EditText
        mJobTimeFrameField = findViewById<View>(R.id.jobtimeframe) as EditText
        mJobDescriptionField = findViewById<View>(R.id.jobdescription) as EditText

        var mJobNameString = mJobNameField.text.toString()
        var mJobTimeFrameString = mJobTimeFrameField.text.toString()
        var mJobDescriptionString = mJobDescriptionField.text.toString()

        mCreateJobButton.setOnClickListener {
            //TODO: Bring to View Job Company View
            if(mJobNameString.isEmpty() || mJobTimeFrameString.isEmpty() || mJobDescriptionString.isEmpty()) {
                if (mJobNameString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Job Name Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }

                if (mJobTimeFrameString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Job Time Frame Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }

                if (mJobDescriptionString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Job Description Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }else{
                startActivity(Intent())
            }

        }
    }
}