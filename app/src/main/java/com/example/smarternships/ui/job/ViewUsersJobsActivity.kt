package com.example.smarternships.ui.job

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class ViewUsersJobsActivity : AppCompatActivity() {

    private lateinit var mListViewJobs : ListView
    private lateinit var allJobIds: MutableList<String>
    private lateinit var allJobNames: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var mUser: User
    private lateinit var mCurrentUser: User
    private var mCurrentUserId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_jobs_list)

        mListViewJobs = findViewById<View>(R.id.listViewJobs) as ListView

        val i = intent
        val b = i.extras

        val userID = b?.getString("USERID")

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
        allJobIds = ArrayList()
        allJobNames = ArrayList()

        adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            allJobNames
        )
        mListViewJobs.adapter = adapter

        if (userID != null) {
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mUser = dataSnapshot?.getValue(User::class.java)!!

                    if(mUser != null && mUser?.jobs != null){
                        mUser?.jobs.forEach { jobId ->
                            if (jobId != null) {
                                DataBase.getJob(jobId, object : OnGetDataListener {
                                    override fun onSuccess(dataSnapshot1: DataSnapshot?) {
                                        var thisJob = dataSnapshot1?.getValue(Job::class.java)

                                        allJobIds.add(jobId)
                                        allJobNames.add(thisJob!!.jobName)

                                        adapter.notifyDataSetChanged()
                                    }
                                    override fun onStart() {}
                                    override fun onFailure() {}
                                })
                            }
                        }

                        // attaching listener to listview
                        mListViewJobs.onItemClickListener = AdapterView.OnItemClickListener { _, _, item, _ ->
                            //getting the selected job
                            val jobId = allJobIds[item]
                            Log.i("onClickItemListenerJobs", jobId)

                            val intent = Intent(applicationContext, ViewJobActivity::class.java)
                            intent.putExtra("JOBID", jobId)
                            startActivity(intent)
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

