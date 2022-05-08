package com.example.smarternships.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.job.CreateJobActivity
import com.example.smarternships.ui.job.MyJobsList
import com.google.firebase.database.DataSnapshot


class ViewAccountActivity: AppCompatActivity() {
    private lateinit var mTextName: EditText
    private lateinit var mTextEmail: EditText
    private lateinit var mTextDescription: EditText
    private lateinit var mViewAllJobs: Button
    private lateinit var mEditButton: Button
    private lateinit var mUser: User

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_account)

        val i = intent
        val e = i.extras

        mTextName = findViewById<View>(R.id.name) as EditText
        mTextEmail = findViewById<View>(R.id.email) as EditText
        mTextDescription = findViewById<View>(R.id.description) as EditText
        mViewAllJobs = findViewById<View>(R.id.view_all_jobs) as Button

        mEditButton = findViewById<View>(R.id.edit_button) as Button

        val userID = e?.getString("USERID");

        if (userID != null) {
//            Toast.makeText(applicationContext, userID, Toast.LENGTH_SHORT).show()

            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mUser = dataSnapshot?.getValue(User::class.java)!!
                    if (mUser != null) {
                        mTextName.setText(mUser.userName)
                        mTextEmail.setText(mUser.userEmail)
                        mTextDescription.setText(mUser.userDescription)
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

        // TODO setup check to see if user being viewed is the logged in user. If it is not then disable and hide edit button

        mViewAllJobs.setOnClickListener {
            Toast.makeText(applicationContext, "Redirect to all Jobs", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MyJobsList::class.java)
            intent.putExtra("USERID",userID)
            startActivity(intent)
        }

        mEditButton.setOnClickListener {
            val intent = Intent(this, EditAccountActivity::class.java)
            intent.putExtra("USERID", userID)
            startActivity(intent)
        }
    }

    // Create Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    // Process clicks on Options Menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_job -> {
                Toast.makeText(applicationContext, "Redirect to find/create job", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CreateJobActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_view_jobs -> {
                Toast.makeText(applicationContext, "View Jobs", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_logout -> {
                Toast.makeText(applicationContext, "Logout User", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}

