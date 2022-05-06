package com.example.smarternships.ui.job

import android.widget.Button

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.databinding.ActivityRegisterBinding
import com.example.smarternships.data.model.Job
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.util.Pair
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.account.ViewAccountActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.firebase.database.DataSnapshot
import org.w3c.dom.Text
import java.util.*

class ManageJobActivity: AppCompatActivity() {

    private lateinit var mJobNameField : EditText
    private lateinit var mJobTimeFrameField : EditText
    private lateinit var mJobDescriptionField : EditText
    private lateinit var mViewInternButton: Button
    private lateinit var mDeleteInternButton: Button
    private lateinit var mUpdateJobButton: Button
    private lateinit var mSelectTimeFrameButton: Button
    private lateinit var mCurrentInternField: EditText


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.create_job)

        val i = intent
        val e = i.extras
        var job = null;

        mUpdateJobButton = findViewById<View>(R.id.createjob) as Button
        mSelectTimeFrameButton = findViewById<View>(R.id.timeframeButton) as Button
        mJobNameField = findViewById<View>(R.id.jobname) as EditText
        mJobTimeFrameField = findViewById<View>(R.id.jobtimeframe) as EditText
        mJobDescriptionField = findViewById<View>(R.id.jobdescription) as EditText
        mViewInternButton = findViewById<View>(R.id.viewCurrentIntern) as Button
        mDeleteInternButton = findViewById<View>(R.id.delCurrentIntern) as Button
        mCurrentInternField = findViewById<View>(R.id.currentIntern) as EditText

        val jobID = e?.getString("JOBID");

        if(jobID != null){
            Toast.makeText(applicationContext, jobID, Toast.LENGTH_SHORT).show()
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var job = dataSnapshot?.getValue(Job::class.java)
                    if (job != null) {
                        mJobNameField.setText(job.jobName)
                        mJobTimeFrameField.setText(job.timeFrame)
                        mJobDescriptionField.setText(job.description)
                        mCurrentInternField.setText(job.assignedUserId)
                    }
                }

                override fun onStart() {
                    //when starting
                    Log.d("ONSTART", "Started")
                }

                override fun onFailure() {
                    Log.d("onFailure", "Failed")
                }
            })
        }



        var mStartDate = "";
        var mEndDate = "";

        //Used to bring up date range Selector
        mSelectTimeFrameButton.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    .setSelection(
                        Pair(
                            MaterialDatePicker.thisMonthInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds()
                        )
                    )
                    .build()

            dateRangePicker.show(supportFragmentManager, "tag");
            dateRangePicker.addOnPositiveButtonClickListener {
                Log.i("DATE PICKER","The selected date range is ${it.first} - ${it.second}")
                mStartDate = SimpleDateFormat("MM/dd/yyyy").format(Date(it.first));
                mEndDate = SimpleDateFormat("MM/dd/yyyy").format(Date(it.second));

                mJobTimeFrameField.text = "${mStartDate} - ${mEndDate}" as Editable
            }

        }

        mViewInternButton.setOnClickListener {
            //TODO: Go To View Intern

        }

        mDeleteInternButton.setOnClickListener {
            //TODO: Delete Intern from job

        }


        mUpdateJobButton.setOnClickListener {
            //TODO: Bring to View Job Company View
            var mJobNameString = mJobNameField.text.toString()
            var mJobTimeFrameString = mJobTimeFrameField.text.toString()
            var mJobDescriptionString = mJobDescriptionField.text.toString()

            if(mJobNameString.isEmpty() ||
                mJobTimeFrameString.isEmpty() ||
                mJobDescriptionString.isEmpty() ||
                mStartDate.isEmpty() ||
                mEndDate.isEmpty()
            ){

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

                if (mStartDate.isEmpty() || mEndDate.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Must Choose a Time Frame",
                        Toast.LENGTH_SHORT
                    ).show();
                }

            }else{
                val companyID = e?.getString("COMPANYID")
                //TODO: GET CURRENT JOB
                if(job != null){
                    //TODO:UPDATE JOB
                    val intent = Intent(this, ViewJobActivity::class.java)
                    intent.putExtra("JOBID", jobID)
                    startActivity(intent)
                }

            }

        }
    }
}