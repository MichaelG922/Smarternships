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
    private lateinit var mManageButton: Button
    private lateinit var mApplicantsButton: Button

    lateinit var mJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.view_job)
        mJobName = findViewById<View>(R.id.job_name) as EditText
        mIntern = findViewById<View>(R.id.intern_name) as EditText
        mCompanyName = findViewById<View>(R.id.company_name) as EditText
        mTimeFrame = findViewById<View>(R.id.time_frame) as EditText
        mJobDescription = findViewById<View>(R.id.description) as EditText

        mViewIntern = findViewById<View>(R.id.view_intern) as Button
        mViewCompany = findViewById<View>(R.id.view_company) as Button
        mApplyButton = findViewById<View>(R.id.apply_button) as Button
        mManageButton = findViewById<View>(R.id.manage_button) as Button
        mApplicantsButton = findViewById<View>(R.id.view_applicants) as Button

        mApplicantsButton.visibility = View.INVISIBLE
        mApplyButton.visibility = View.INVISIBLE
        mManageButton.visibility = View.INVISIBLE

        val i = intent
        val b = i.extras

        val jobID = b?.getString("JOBID")

        if(jobID != null){
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mJob = dataSnapshot?.getValue(Job::class.java)!!
                    if (mJob != null) {
                        mJobName.setText(mJob.jobName)
                        mTimeFrame.setText(mJob.timeFrame)
                        mJobDescription.setText(mJob.description)

                        // set company name to be name corresponding to company id
                        DataBase.getUser(mJob.companyId, object : OnGetDataListener {
                            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                                var user = dataSnapshot?.getValue(User::class.java)!!
                                if (user != null) {
                                    mCompanyName.setText(user.userName)
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

                        if (mJob.assignedUserId != "") {
                            // set assigned user name to be name corresponding to assigned user id
                            DataBase.getUser(mJob.assignedUserId, object : OnGetDataListener {
                                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                                    var user = dataSnapshot?.getValue(User::class.java)!!
                                    if (user != null) {
                                        mIntern.setText(user.userName)
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
                            mIntern.setText("Job Not Assigned")
                            mViewIntern.visibility = View.INVISIBLE
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

        val userID = b?.getString("USERID")
        if(userID != null){
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)
                    if (user != null) {
                        if(user.isIntern){
                            //unapplied intern: show apply button & hide intern fieldi
                            if(!mJob.applicants.contains(b?.getString("USERID"))){
                                mApplyButton.visibility = View.VISIBLE
                                mIntern.visibility = View.INVISIBLE
                            }
                            //company: show applicants & manage buttons
                        } else if(!user.isIntern){
                            mApplicantsButton.visibility = View.VISIBLE
                            mManageButton.visibility = View.VISIBLE

                            //applied intern: hide apply button & show applicant name
                        } else{
                            mApplyButton.visibility = View.INVISIBLE
                            mIntern.visibility = View.VISIBLE
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

        //when a company account hits the applicants button
        mApplicantsButton.setOnClickListener {

        }

        mViewCompany.setOnClickListener {
            val intent = Intent(this, ViewAccountActivity::class.java)
            intent.putExtra("USERID", mJob.companyId)
            startActivity(intent)
        }

        mViewIntern.setOnClickListener {
            val intent = Intent(this, ViewAccountActivity::class.java)
            intent.putExtra("USERID", mJob.assignedUserId)
            startActivity(intent)
        }

        mManageButton.setOnClickListener {
            val intent = Intent(this, ManageJobActivity::class.java)
            intent.putExtra("JOBID", jobID)
            startActivity(intent)
        }

        mApplyButton.setOnClickListener {
            val tempApps = mutableListOf<String>()
            mJob.applicants.forEach {
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
                DataBase.setJob(jobID!!, editedJob)
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