package com.example.smarternships.ui.job

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.account.ViewAccountActivity
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot


class ViewApplicantsActivity : AppCompatActivity() {

    private lateinit var mListViewUsers : ListView
    private lateinit var mCurrentUser: User
    private lateinit var mUserIds: MutableList<String>
    private lateinit var mUserNames: MutableList<String>
    private lateinit var mAdapter: ArrayAdapter<String>
    private lateinit var mJob: Job
    private lateinit var mContext: Context
    private var mCurrentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_list)

        (findViewById<View>(R.id.textView2) as TextView).text = "Applicants"

        mListViewUsers = findViewById<View>(R.id.listViewJobs) as ListView

        mContext = this

        val i = intent
        val b = i.extras

        val jobID = b?.getString("JOBID")

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

        //List to store all jobs
        mUserIds = ArrayList()
        mUserNames = ArrayList()

        mAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mUserNames
        )
        mListViewUsers.adapter = mAdapter

        if (jobID != null) {
            DataBase.getJob(jobID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mJob = dataSnapshot?.getValue(Job::class.java)!!
                    Log.i("mJobForApplicants", mJob.toString())

                    if(mJob != null && mJob?.applicants != null){
                        mJob?.applicants.forEach { userId ->

                            if (userId != null) {
                                DataBase.getUser(userId, object : OnGetDataListener {
                                    override fun onSuccess(dataSnapshot1: DataSnapshot?) {
                                        var thisUser = dataSnapshot1?.getValue(User::class.java)
                                        if (!mUserIds.contains(userId)) {
                                            mUserIds.add(userId)
                                            mUserNames.add(thisUser!!.userName)

                                            mAdapter.notifyDataSetChanged()
                                        }
                                    }
                                    override fun onStart() {}
                                    override fun onFailure() {}
                                })
                            }
                        }

                        // attaching listener to listview
                        mListViewUsers.onItemClickListener = AdapterView.OnItemClickListener { _, _, item, _ ->
                            //getting the selected job
                            val userId = mUserIds[item]
                            val dialogClickListener =
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            mJob.assignedUserId = userId
                                            DataBase.setJob(jobID, mJob)
                                            DataBase.addJobToUser(jobID, userId)
                                            val intent = Intent(applicationContext, ViewJobActivity::class.java)
                                            intent.putExtra("JOBID", jobID)
                                            startActivity(intent)
                                        }
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                            val intent = Intent(applicationContext, ViewAccountActivity::class.java)
                                            intent.putExtra("USERID", userId)
                                            startActivity(intent)
                                        }
                                    }
                                }

                            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
                            builder.setMessage("View Account or Accept Application")
                                .setPositiveButton("Accept", dialogClickListener)
                                .setNegativeButton("View", dialogClickListener).show()
                            if (mCurrentUserId == mJob.companyId) {

                            } else {
                                val intent = Intent(applicationContext, ViewAccountActivity::class.java)
                                intent.putExtra("USERID", userId)
                                startActivity(intent)
                            }
                        }
                    }
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
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
