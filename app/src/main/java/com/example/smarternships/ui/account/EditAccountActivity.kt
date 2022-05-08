package com.example.smarternships.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.job.CreateJobActivity
import com.example.smarternships.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class EditAccountActivity : AppCompatActivity() {
    private lateinit var mEditNameView: EditText
    private lateinit var mEmailView: EditText
    private lateinit var mDescriptionView: EditText
    private lateinit var mSaveButton: Button
    private lateinit var mUser: User
    private lateinit var mCurrentUser: User
    private var mCurrentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_account)

        mEditNameView = findViewById<View>(R.id.edit_name) as EditText
        mEmailView = findViewById<View>(R.id.edit_email) as EditText
        mDescriptionView = findViewById<View>(R.id.edit_description) as EditText

        mSaveButton = findViewById<View>(R.id.save_button) as Button

        // get the user id of the user to display
        val i = intent
        val e = i.extras
        val userID = e?.getString("USERID")

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

        if (userID != null) {
            Toast.makeText(applicationContext, userID, Toast.LENGTH_SHORT).show()

            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mUser = dataSnapshot?.getValue(User::class.java)!!
                    mEditNameView.setText(mUser.userName)
                    mEmailView.setText(mUser.userEmail)
                    mDescriptionView.setText(mUser.userDescription)

                    if(mUser.userType == "Intern") {
                        mDescriptionView.hint = "Resume"
                    } else {
                        mDescriptionView.hint = "Company Description"
                    }
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        mSaveButton.setOnClickListener {
            var mNameString = mEditNameView.text.toString()
            var mDescriptionString = mDescriptionView.text.toString()

            if(mNameString.isEmpty() || mDescriptionString.isEmpty()) {
                if (mNameString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Name Name Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }

                if (mDescriptionString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Description Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }

            }else{
                if (userID != null) {
                    DataBase.getUser(userID, object : OnGetDataListener {
                        override fun onSuccess(dataSnapshot: DataSnapshot?) {
                            val user = dataSnapshot?.getValue(User::class.java)
                            if (user != null) {
                                user.userName = mNameString
                                user.userDescription = mDescriptionString
                                DataBase.setUser(userID, user)
                            }
                        }
                        override fun onStart() {}
                        override fun onFailure() {}
                    })
                }
                val intent = Intent(this, ViewAccountActivity::class.java)
                intent.putExtra("USERID", userID)
                startActivity(intent)
            }
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