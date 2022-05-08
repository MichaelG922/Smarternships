package com.example.smarternships.ui.job

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference


//
//import com.example.smarternships.data.model.Job
//import com.example.smarternships.data.model.OnGetDataListener
//import com.example.smarternships.data.model.User
//import com.example.smarternships.ui.account.ViewAccountActivity
private lateinit var databaseUsers: DatabaseReference

class MyJobsList : AppCompatActivity() {

    private lateinit var mTextView : TextView
    private lateinit var mListViewJobs : ListView
    private lateinit var allJobs: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.my_jobs_list)

        mTextView = findViewById<View>(R.id.job_feed_view) as TextView
        mListViewJobs = findViewById<View>(R.id.listViewJobs) as ListView

        //List to store all jobs
        allJobs = ArrayList()

        //Todo add listener for when a job is clicked?
//        //attaching listener to listview
//        mListViewJobs.onItemClickListener = AdapterView.OnItemClickListener { _, _, item, _ ->
//            //getting the selected artist
//            val author = allJobs[item]

//            //creating an intent
//            val intent = Intent(applicationContext, AuthorActivity::class.java)
//
//            //putting artist name and id to intent
//            intent.putExtra(AUTHOR_ID, author.authorId)
//            intent.putExtra(AUTHOR_NAME, author.authorName)
//
//            //starting the activity with intent
//            startActivity(intent)
//        }

        val i = intent
        val b = i.extras

        adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            allJobs
        )
        mListViewJobs.adapter = adapter

        val userID = b?.getString("USERID")
        if (userID != null) {
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)

                    if(user != null){
                        var listOfJobs = user?.jobs
                        listOfJobs.forEach { x ->
                            DataBase.getJob(x, object : OnGetDataListener {
                                override fun onSuccess(dataSnapshot1: DataSnapshot?) {

                                    var thisJob = dataSnapshot1?.getValue(Job::class.java)
                                    allJobs.add(thisJob!!.jobName)
                                    adapter.notifyDataSetChanged()
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
    }
}
//}

