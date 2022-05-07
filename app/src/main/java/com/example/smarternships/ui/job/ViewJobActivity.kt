package com.example.smarternships.ui.job

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.account.ViewAccountActivity
import com.google.firebase.database.DataSnapshot

class ViewJobActivity : AppCompatActivity() {

    private lateinit var mJobName: EditText
    private lateinit var mCompanyName: EditText
    private lateinit var mTimeFrame: EditText
    private lateinit var mIntern: EditText
    private lateinit var mJobDescription : EditText
    private lateinit var mViewIntern: Button
    private lateinit var mViewCompany: Button
    private lateinit var mApplyButton: Button
    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_view_job)
        mJobName = findViewById<View>(R.id.job_name) as EditText
        mIntern = findViewById<View>(R.id.intern_name) as EditText
        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.description) as EditText

        mViewIntern = findViewById<View>(R.id.view_intern) as Button
        mViewCompany = findViewById<View>(R.id.view_company) as Button
        mApplyButton = findViewById<View>(R.id.apply_button) as Button
        mApplyButton.visibility = View.INVISIBLE

        val i = intent
        val b = i.extras

        val jobID = b?.getString("JOBID")

        if(jobID != null){
            Toast.makeText(applicationContext, jobID, Toast.LENGTH_SHORT).show()
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    job = dataSnapshot?.getValue(Job::class.java)!!
                    if (job != null) {
                        mJobName.setText(job.jobName)
                        mTimeFrame.setText(job.timeFrame)
                        mJobDescription.setText(job.description)
                        mCompanyName.setText(job.companyId)
                        mIntern.setText(job.assignedUserId)
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

        val userID = b?.getString("USERID")
        if(userID != null){
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)
                    if (user != null) {
                        if(user.isIntern){
                            if(!job.applicants.contains(b?.getString("USERID"))){
                                mApplyButton.visibility = View.VISIBLE
                                mIntern.visibility = View.INVISIBLE
                            }
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


        mViewCompany.setOnClickListener {
            var companyID = mCompanyName.text.toString()
            val intent = Intent(this, ViewAccountActivity::class.java)
            intent.putExtra(b?.getString("USERID"), companyID)
            startActivity(intent)
        }

        mViewIntern.setOnClickListener {
            var currentInternId = mIntern.text.toString();
            val intent = Intent(this, ViewAccountActivity::class.java)
            intent.putExtra(b?.getString("USERID"), currentInternId)
            startActivity(intent)
        }

        mApplyButton.setOnClickListener {
            val tempApps = mutableListOf<String>()
            job.applicants.forEach {
                tempApps.add(it)
            }
            tempApps.add(b?.getString("USERID")!!)

            val companyID = b?.getString("USERID")
            var editedJob = companyID?.let { it1 -> Job(
                jobName = mJobName.text.toString(),
                companyId = it1,
                description = mJobDescription.text.toString(),
                timeFrame = mTimeFrame.text.toString(),
                applicants = tempApps
            ) }
            if(editedJob != null) {
                DataBase.createJob(jobID!!, editedJob)
                val intent = Intent(this, ViewJobActivity::class.java)
                intent.putExtra("JOBID", jobID)
                startActivity(intent)
            }

            Toast.makeText(
                this,
                "Applied to Job!",
                Toast.LENGTH_SHORT
            ).show();
        }
    }
}