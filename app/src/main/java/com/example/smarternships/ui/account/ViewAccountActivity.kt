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
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class ViewAccountActivity: AppCompatActivity() {
    private lateinit var mTextName: EditText
    private lateinit var mTextEmail: EditText
    private lateinit var mTextDescription: EditText
    private lateinit var mViewCurrentJobs: Button
    private lateinit var mViewPriorJobs: Button
    private lateinit var mEditButton: Button
    private lateinit var mUser: User
    private lateinit var mCurrentUser: User
    private var mCurrentUserId: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_account)

        mTextName = findViewById<View>(R.id.name) as EditText
        mTextEmail = findViewById<View>(R.id.email) as EditText
        mTextDescription = findViewById<View>(R.id.description) as EditText

        mViewCurrentJobs = findViewById<View>(R.id.viewCurrentJobs) as Button
        mViewPriorJobs = findViewById<View>(R.id.viewPriorJobs) as Button
        mEditButton = findViewById<View>(R.id.edit_button) as Button

        mEditButton.visibility = View.INVISIBLE

        // get the user id of the user to display
        val i = intent
        val e = i.extras
        val userID = e?.getString("USERID")

        // get the current user
        var auth = FirebaseAuth.getInstance()
        mCurrentUserId = auth.currentUser?.uid

        // if its not the current user then don't let them edit
        if (userID == mCurrentUserId) {
            mEditButton.visibility = View.VISIBLE
        }

        DataBase.getUser(mCurrentUserId!!, object : OnGetDataListener {
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                mCurrentUser = dataSnapshot?.getValue(User::class.java)!!
                Log.i("LoggedInUserInfo", mCurrentUser.toString())
            }
            override fun onStart() {}
            override fun onFailure() {}
        })

        // get the details of the user that is being shown
        if (userID != null) {
            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mUser = dataSnapshot?.getValue(User::class.java)!!
                    mTextName.setText(mUser.userName)
                    mTextEmail.setText(mUser.userEmail)
                    mTextDescription.setText(mUser.userDescription)

                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        mViewCurrentJobs.setOnClickListener {
            // TODO redirect to current jobs
            Toast.makeText(applicationContext, "Redirect to users Current Jobs", Toast.LENGTH_SHORT).show()
        }

        mViewPriorJobs.setOnClickListener {
            // TODO redirect to prior jobs
            Toast.makeText(applicationContext, "Redirect to users Prior Jobs", Toast.LENGTH_SHORT).show()
        }

        // send the user to edit account page
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
                    Toast.makeText(applicationContext, "Redirect to View Jobs", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, CreateJobActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.action_view_jobs -> {
                Toast.makeText(applicationContext, "View Jobs", Toast.LENGTH_SHORT).show()
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

