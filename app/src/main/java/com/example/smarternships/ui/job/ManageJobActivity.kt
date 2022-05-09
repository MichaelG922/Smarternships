package com.example.smarternships.ui.job

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Button

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.data.model.Job
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.util.Pair
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.account.ViewAccountActivity
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
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
    private lateinit var mCurrentUser: User
    private lateinit var mJob: Job
    private var mCurrentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_job)

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

        val i = intent
        val e = i.extras
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
                                override fun onStart() {}
                                override fun onFailure() {}
                            })
                        } else {
                            mCurrentInternField.setText("Job Not Assigned")
                            mViewInternButton.visibility = View.INVISIBLE
                            mDeleteInternButton.visibility = View.INVISIBLE
                        }
                    }
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        // get the current user
        var auth = FirebaseAuth.getInstance()
        mCurrentUserId = auth.currentUser?.uid

        DataBase.getUser(mCurrentUserId!!, object : OnGetDataListener {
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                mCurrentUser = dataSnapshot?.getValue(User::class.java)!!
                Log.i("LoggedInUserInfo", mCurrentUser.toString())
            }
            override fun onStart() {}
            override fun onFailure() {}
        })

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
                    finish()
                }
            }
        }

        mDeleteButton.setOnClickListener {
            if(mJob != null && jobID != null) {
                if (mJob.assignedUserId != "") {
                    DataBase.removeJobFromUser(jobID, mJob.assignedUserId)
                }
                DataBase.removeJobFromUser(jobID, mJob.companyId)
                DataBase.deleteJob(jobID!!)

                val intent = Intent(this, ViewAccountActivity::class.java)
                intent.putExtra("USERID", mCurrentUserId)
                startActivity(intent)
                finish()
            }
        }

        mFinishButton.setOnClickListener {
            if(mJob != null) {

                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                mJob.completed = true
                                mJob.timeFrame = "Completed"
                                DataBase.setJob(jobID!!, mJob)

                                val intent = Intent(this, ViewJobActivity::class.java)
                                intent.putExtra("JOBID", jobID)
                                startActivity(intent)
                                finish()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {

                            }
                        }
                    }
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to mark this job as complete?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show()
            }
        }
    }


    // Create Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    // Process clicks on hamburger
    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        var menuItem = menu.findItem(R.id.action_job) as MenuItem
        if (mCurrentUser.userType == "Intern") {
            menuItem.title = "Find Jobs"
        } else {
            menuItem.title = "Create Job"
        }
        return false
    }

    // Process clicks on Options Menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_job -> {
                if (mCurrentUser.userType == "Intern") {
                    val intent = Intent(this, ViewJobsActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, CreateJobActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.action_view_jobs -> {
                val intent = Intent(this, ViewUsersJobsActivity::class.java)
                intent.putExtra("USERID", mCurrentUserId)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                // force login again if they logout, dont let them go back
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}