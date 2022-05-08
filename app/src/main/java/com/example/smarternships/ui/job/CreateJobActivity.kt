package com.example.smarternships.ui.job

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
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import java.util.*

class CreateJobActivity: AppCompatActivity() {

    private lateinit var mJobNameField : EditText
    private lateinit var mJobTimeFrameField : EditText
    private lateinit var mJobDescriptionField : EditText
    private lateinit var mCreateJobButton: Button
    private lateinit var mSelectTimeFrameButton: Button
    private var mCurrentUserId: String? = null
    private lateinit var mCurrentUser: User

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_job)

        mCreateJobButton = findViewById<View>(R.id.createjob) as Button
        mSelectTimeFrameButton = findViewById<View>(R.id.timeframeButton) as Button
        mJobNameField = findViewById<View>(R.id.jobname) as EditText
        mJobTimeFrameField = findViewById<View>(R.id.jobtimeframe) as EditText
        mJobDescriptionField = findViewById<View>(R.id.jobdescription) as EditText

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

                mJobTimeFrameField.setText("${mStartDate} - ${mEndDate}");            }

        }

        mCreateJobButton.setOnClickListener {
            var mJobNameString = mJobNameField.text.toString()
            var mJobTimeFrameString = mJobTimeFrameField.text.toString()
            var mJobDescriptionString = mJobDescriptionField.text.toString()

            if(mJobNameString.isEmpty() || mJobTimeFrameString.isEmpty() ||
                mJobDescriptionString.isEmpty() || mStartDate.isEmpty() || mEndDate.isEmpty()){

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

                var job = mCurrentUserId?.let { userId ->
                        Job(
                            jobName = mJobNameString,
                            companyId = userId,
                            description = mJobDescriptionString,
                            timeFrame = "$mStartDate - $mEndDate",
                        )}

                if(job != null){
                    val newJobID = java.util.UUID.randomUUID().toString()
                    DataBase.setJob(newJobID, job)

                    mCurrentUserId?.let { it1 -> DataBase.addJobToUser(newJobID, it1) }

                    val intent = Intent(this, ViewJobActivity::class.java)
                    intent.putExtra("JOBID", newJobID)
                    startActivity(intent)
                }

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