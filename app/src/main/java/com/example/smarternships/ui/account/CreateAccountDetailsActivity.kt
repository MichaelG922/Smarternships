package com.example.smarternships.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.User
import com.google.firebase.auth.FirebaseAuth


class CreateAccountDetailsActivity : AppCompatActivity() {

    private lateinit var mContinueButton: Button
    private lateinit var mNameTextView: EditText
    private lateinit var mDescriptionTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.create_account_details)

        val i = intent
        val b = i.extras

        val isIntern = b?.getBoolean("ISINTERN")

        mContinueButton = findViewById<View>(R.id.continue_button) as Button
        mNameTextView = findViewById<View>(R.id.name_editText) as EditText
        mDescriptionTextView = findViewById<View>(R.id.description_editText) as EditText

        if (isIntern == true) {
            mDescriptionTextView.hint = "Resume";
        } else {
            mDescriptionTextView.hint = "Company Description";
        }

        mContinueButton.setOnClickListener {
            var mNameString = mNameTextView.text.toString()
            var mDescriptionString = mDescriptionTextView.text.toString()

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
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                var userEmail = firebaseUser?.email
                var userID = firebaseUser?.uid

                var user = isIntern?.let { isInt -> User(mNameString, userEmail!!, mDescriptionString, isInt) }
                if (user != null) {
                    DataBase.setUser(userID!!, user)
                    val intent = Intent(this, ViewAccountActivity::class.java)
                    intent.putExtra("USERID", userID)
                    startActivity(intent)
                }
            }
        }
    }
}