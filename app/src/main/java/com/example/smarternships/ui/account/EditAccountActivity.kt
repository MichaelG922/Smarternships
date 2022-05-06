package com.example.smarternships.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase
import com.example.smarternships.data.model.User

class EditAccountActivity : AppCompatActivity() {

    private lateinit var mEditNameView: EditText
    private lateinit var mEmailView: EditText
    private lateinit var mDescriptionView: EditText
    private lateinit var mSaveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.edit_account)

        val i = intent
        val b = i.extras

        val isIntern = b?.getBoolean("ISINTERN")

        //TODO - populate these fields properly
        mEditNameView = findViewById<View>(R.id.edit_name) as EditText
        mEmailView = findViewById<View>(R.id.edit_email) as EditText
        mDescriptionView = findViewById<View>(R.id.edit_description) as EditText
        mSaveButton = findViewById<View>(R.id.save_button) as Button

        if(isIntern == true){
            mDescriptionView.hint = "Resume"
        } else {
            mDescriptionView.hint = "Company Description"
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
                //TODO- update in DB & go back to prev activity
                setResult(RESULT_OK)
            }
        }
    }
}