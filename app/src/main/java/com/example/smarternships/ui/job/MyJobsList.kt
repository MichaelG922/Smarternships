package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//
//import com.example.smarternships.data.model.Job
//import com.example.smarternships.data.model.OnGetDataListener
//import com.example.smarternships.data.model.User
//import com.example.smarternships.ui.account.ViewAccountActivity
private lateinit var databaseUsers: DatabaseReference

class MyJobsList : AppCompatActivity() {

    private lateinit var mTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.my_jobs_list)

        mTextView = findViewById<View>(R.id.job_feed_view) as TextView
        val i = intent
        val b = i.extras

        val userID = b?.getString("USERID")
        if (userID != null) {
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)

                    if(user != null){
                        var listOfJobs = user?.currentJobs
                        listOfJobs.forEach { x ->
                            DataBase.getJob(x.assignedUserId, object : OnGetDataListener {
                                override fun onSuccess(dataSnapshot1: DataSnapshot?) {

                                    var thisJob = dataSnapshot1?.getValue(Job::class.java)

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
                    //if (user != null) {
//
//    var listOfJobs = user?.currentJobs
//    listOfJobs.forEach { x ->
//
//        var jobId = DataBase.getJob(x.assignedUserId, object : OnGetDataListener{
//
//        })
////                            var thisJob = DataBase.getJob(x,object : OnGetDataListener{
//
//    }
//}

//                        if(user.isIntern){
//                            if(!job.applicants.contains(b?.getString("USERID"))){
//                                mApplyButton.visibility = View.VISIBLE
//                                mIntern.visibility = View.INVISIBLE
//                            }
//                        }
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

