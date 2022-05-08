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
import com.google.firebase.database.DataSnapshot

class EditAccountActivity : AppCompatActivity() {

    private lateinit var mEditNameView: EditText
    private lateinit var mEmailView: EditText
    private lateinit var mDescriptionView: EditText
    private lateinit var mSaveButton: Button
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.edit_account)

        val i = intent
        val e = i.extras

        val userID = e?.getString("USERID");

        mEditNameView = findViewById<View>(R.id.edit_name) as EditText
        mEmailView = findViewById<View>(R.id.edit_email) as EditText
        mDescriptionView = findViewById<View>(R.id.edit_description) as EditText
        mSaveButton = findViewById<View>(R.id.save_button) as Button

        if (userID != null) {
            Toast.makeText(applicationContext, userID, Toast.LENGTH_SHORT).show()

            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mUser = dataSnapshot?.getValue(User::class.java)!!
                    if (mUser != null) {
                        mEditNameView.setText(mUser.userName)
                        mEmailView.setText(mUser.userEmail)
                        mDescriptionView.setText(mUser.userDescription)

                        if(mUser.isIntern) {
                            mEditNameView.hint = "User"
                            mDescriptionView.hint = "Resume"
                        } else {
                            mEditNameView.hint = "Company"
                            mDescriptionView.hint = "Company Description"
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

                        override fun onStart() {
                            //when starting
                            Log.d("ONSTART", "Started")
                        }

                        override fun onFailure() {
                            Log.d("onFailure", "Failed")
                        }
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