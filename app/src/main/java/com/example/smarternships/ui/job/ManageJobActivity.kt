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

    private lateinit var mDeleteButton: Button
    private lateinit var mFinishButton: Button

    lateinit var mJob: Job

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.manage_job)

        val i = intent
        val e = i.extras

        mJobNameField = findViewById<View>(R.id.jobName) as EditText
        mJobTimeFrameField = findViewById<View>(R.id.jobTimeFrame) as EditText
        mJobDescriptionField = findViewById<View>(R.id.jobDescription) as EditText
        mCurrentInternField = findViewById<View>(R.id.currentIntern) as EditText

        mUpdateJobButton = findViewById<View>(R.id.save_button) as Button
        mSelectTimeFrameButton = findViewById<View>(R.id.timeframeButton) as Button
        mViewInternButton = findViewById<View>(R.id.viewCurrentIntern) as Button
        mDeleteInternButton = findViewById<View>(R.id.delCurrentIntern) as Button
        mDeleteButton = findViewById<View>(R.id.delete_button) as Button
        mFinishButton = findViewById<View>(R.id.finish_button) as Button

        val jobID = e?.getString("JOBID");

        if(jobID != null){
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mJob = dataSnapshot?.getValue(Job::class.java)!!
                    if (mJob != null) {
                        mJobNameField.setText(mJob.jobName)
                        mJobTimeFrameField.setText(mJob.timeFrame)
                        mJobDescriptionField.setText(mJob.description)

                        if (mJob.assignedUserId != "") {
                            // set assigned user name to be name corresponding to assigned user id
                            DataBase.getUser(mJob.assignedUserId, object : OnGetDataListener {
                                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                                    var user = dataSnapshot?.getValue(User::class.java)!!
                                    if (user != null) {
                                        mCurrentInternField.setText(user.userName)
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
                        } else {
                            mCurrentInternField.setText("Job Not Assigned")
                            mViewInternButton.visibility = View.INVISIBLE
                            mDeleteInternButton.visibility = View.INVISIBLE
                        }
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

                mJobTimeFrameField.setText("${mStartDate} - ${mEndDate}");
            }

        }

        mViewInternButton.setOnClickListener {
                // Go To View Intern
                val intent = Intent(this, ViewAccountActivity::class.java)
                intent.putExtra("USERID", mJob.assignedUserId)
                startActivity(intent)
        }

        mDeleteInternButton.setOnClickListener {
            mCurrentInternField.setText("");
        }


        mUpdateJobButton.setOnClickListener {
            //TODO: Bring to View Job Company View
            var mJobNameString = mJobNameField.text.toString()
            var mJobTimeFrameString = mJobTimeFrameField.text.toString()
            var mJobDescriptionString = mJobDescriptionField.text.toString()

            if(mJobNameString.isEmpty()
                || mJobTimeFrameString.isEmpty()
                || mJobDescriptionString.isEmpty()){

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
                if(mJob != null) {
                    mJob.jobName = mJobNameString
                    mJob.description = mJobDescriptionString
                    mJob.timeFrame = mJobTimeFrameString

                    if (mCurrentInternField.text.toString() == "") {
                        // If were removing our intern remove the job from their current jobs
                        if (jobID != null) {
                            DataBase.removeJobFromUser(jobID, mJob.assignedUserId)
                        }
                        mJob.assignedUserId = ""
                    }

                    DataBase.setJob(jobID!!, mJob)
                    val intent = Intent(this, ViewJobActivity::class.java)
                    intent.putExtra("JOBID", jobID)
                    startActivity(intent)
                }
            }

            mDeleteButton.setOnClickListener {
                if(mJob != null) {
                    if (jobID != null) {
                        if (mJob.assignedUserId != "") {
                            DataBase.removeJobFromUser(jobID, mJob.assignedUserId)
                        }
                        DataBase.removeJobFromUser(jobID, mJob.companyId)
                        DataBase.deleteJob(jobID!!)
                    }

                    val intent = Intent(this, ViewAccountActivity::class.java)
                    // TODO send user back to their account need to get their user id
                    intent.putExtra("USERID", "2")
                    startActivity(intent)
                }
            }

            mFinishButton.setOnClickListener {
                if(mJob != null) {
                    mJob.completed = true
                    DataBase.setJob(jobID!!, mJob)
                    val intent = Intent(this, ViewJobActivity::class.java)
                    intent.putExtra("JOBID", jobID)
                    startActivity(intent)
                }
            }

        }
    }
}