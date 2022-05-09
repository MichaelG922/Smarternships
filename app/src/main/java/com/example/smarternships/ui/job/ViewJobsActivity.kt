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
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class ViewJobsActivity : AppCompatActivity() {

    private lateinit var mListViewJobs : ListView
    private lateinit var allJobIds: MutableList<String>
    private lateinit var allJobNames: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var mCurrentUser: User
    private var mCurrentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_list)

        mListViewJobs = findViewById<View>(R.id.listViewJobs) as ListView

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

        DataBase.getAllJobs(object : OnGetDataListener {
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                var jobs = dataSnapshot?.value!! as HashMap<String, Map<*, *>>

                for ((jobId, job) in jobs) {
                    // only show jobs that have not been assigned to a user
                    if (job["assignedUserId"] as String == "") {
                        if (!allJobIds.contains(jobId)) {
                            allJobIds.add(jobId)
                            allJobNames.add(job["jobName"] as String)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                // attaching listener to listview
                mListViewJobs.onItemClickListener = AdapterView.OnItemClickListener { _, _, item, _ ->
                    //getting the selected job
                    val jobId = allJobIds[item]

                    val intent = Intent(applicationContext, ViewJobActivity::class.java)
                    intent.putExtra("JOBID", jobId)
                    startActivity(intent)
                }

            }
            override fun onStart() {}
            override fun onFailure() {}
        })

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
                val intent = Intent(this, ViewJobsActivity::class.java)
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

