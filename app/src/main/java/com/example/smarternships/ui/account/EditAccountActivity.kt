package com.example.smarternships.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.OnGetDataListener
import com.example.smarternships.data.model.User
import com.example.smarternships.ui.job.ViewJobActivity
import com.google.firebase.database.DataSnapshot

class EditAccountActivity : AppCompatActivity() {

    private lateinit var mEditNameView: EditText
    private lateinit var mEmailView: EditText
    private lateinit var mDescriptionView: EditText
    private lateinit var mSaveButton: Button
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.edit_account)

        val i = intent
        val b = i.extras

        val userID = b?.getString("USERID");

        mEditNameView = findViewById<View>(R.id.edit_name) as EditText
        mEmailView = findViewById<View>(R.id.edit_email) as EditText
        mDescriptionView = findViewById<View>(R.id.edit_description) as EditText
        mSaveButton = findViewById<View>(R.id.save_button) as Button

        if (userID != null) {
            Toast.makeText(applicationContext, userID, Toast.LENGTH_SHORT).show()

            DataBase.getUser(userID, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    user = dataSnapshot?.getValue(User::class.java)!!
                    if (user != null) {
                        mEditNameView.setText(user.userName)
                        mEmailView.setText(user.userEmail)
                        mDescriptionView.setText(user.userDescription)
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
            var mEmailString = mEmailView.text.toString()

            if(mNameString.isEmpty() || mDescriptionString.isEmpty() || mEmailString.isEmpty()) {
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

                if(mEmailString.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Empty Email Field not allowed!",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }else{
                val userID = b?.getString("USERID")
                var editedUser = userID?.let{ it -> User(
                    userName = mNameString,
                    userEmail = mEmailString,
                    userDescription = mDescriptionString,
                    isIntern = user.isIntern,
                    currentJobs = user.currentJobs,
                    completedJobs = user.completedJobs
                )}

                if(editedUser != null){
                    DataBase.createUser(userID!!,editedUser)
                    val intent = Intent(this, ViewAccountActivity::class.java)
                    intent.putExtra("USERID", userID)
                    startActivity(intent)
                }
            }
        }
    }
}