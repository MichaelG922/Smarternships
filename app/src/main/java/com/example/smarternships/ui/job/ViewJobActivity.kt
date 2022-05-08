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
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var mJob: Job
    private lateinit var mCurrentUser: User
    private var mCurrentUserId: String? = null
    private lateinit var mApplicantsButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        mApplyButton.visibility = View.INVISIBLE
        mManageButton.visibility = View.INVISIBLE
        mViewIntern.visibility = View.INVISIBLE
        mApplicantsButton.visibility = View.INVISIBLE

        val i = intent
        val b = i.extras
        val jobID = b?.getString("JOBID")

        // get the current user
        var auth = FirebaseAuth.getInstance()
        mCurrentUserId = auth.currentUser?.uid

        if(jobID != null){
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mJob = dataSnapshot?.getValue(Job::class.java)!!
                    mJobName.setText(mJob.jobName)
                    mTimeFrame.setText(mJob.timeFrame)
                    mJobDescription.setText(mJob.description)

                    // set company name to be name corresponding to company id
                    DataBase.getUser(mJob.companyId, object : OnGetDataListener {
                        override fun onSuccess(dataSnapshot: DataSnapshot?) {
                            var user = dataSnapshot?.getValue(User::class.java)!!
                            mCompanyName.setText(user.userName)
                        }
                        override fun onStart() {}
                        override fun onFailure() {}
                    })

                    DataBase.getUser(mCurrentUserId!!, object : OnGetDataListener {
                        override fun onSuccess(dataSnapshot: DataSnapshot?) {
                            mCurrentUser = dataSnapshot?.getValue(User::class.java)!!
                            Log.i("LoggedInUserInfo", mCurrentUser.toString())

                            // if the current is an intern that has not applied let them apply
                            if(mCurrentUser.userType == "Intern"
                                && !mJob.applicants.contains(b?.getString("USERID"))){
                                mApplyButton.visibility = View.VISIBLE
                            }

                            // if the current user is the owner let them manage the job
                            if(mCurrentUserId == mJob.companyId) {
                                mManageButton.visibility = View.VISIBLE
                            }
                        }
                        override fun onStart() {}
                        override fun onFailure() {}
                    })

                    if (mJob.assignedUserId != "") {
                        // set assigned user name to be name corresponding to assigned user id
                        DataBase.getUser(mJob.assignedUserId, object : OnGetDataListener {
                            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                                var user = dataSnapshot?.getValue(User::class.java)!!
                                mIntern.setText(user.userName)
                            }
                            override fun onStart() {}
                            override fun onFailure() {}
                        })
                        mViewIntern.visibility = View.VISIBLE
                    } else {
                        mIntern.setText("Job Not Assigned")
                    }
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        //show view with all applicants
        mApplicantsButton.setOnClickListener {
            val intent = Intent(this, CompanyApplicantList::class.java)
            intent.putExtra("APPLICANTS", arrayOf(mJob.applicants))
            startActivity(intent)
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
            if (jobID != null) {
                mCurrentUserId?.let { it1 -> DataBase.applyToJob(jobID, it1) }
            }

            Toast.makeText(this, "Applied to Job!", Toast.LENGTH_SHORT).show();
        }
    }
}