package com.example.smarternships.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.User


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

        val isIntern = b?.getBoolean("ISINTERN");

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
                var user = isIntern?.let { isInt -> User(mNameString, mDescriptionString, isInt) }
                if (user != null) {
                    DataBase.createUser("2", user)
                    Toast.makeText(applicationContext, "Account Created", Toast.LENGTH_SHORT).show()
                }
                // TODO send user to their account
                // startActivity(Intent())
            }
        }
    }
}