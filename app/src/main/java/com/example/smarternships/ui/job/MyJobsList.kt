package com.example.smarternships.ui.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.Job
import com.example.smarternships.data.model.OnGetDataListener
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


//
//        databaseUsers = FirebaseDatabase.getInstance().getReference("users")
//
//        val isIntern = b?.getBoolean("ISINTERN")
//        //todo - populate job list based on if intern or company (buttons?)
//        val id = databaseUsers.push().key
//        val job = DataBase.getJob(id!!, listener : OnGetDataListener{
//
//        })
    }
}