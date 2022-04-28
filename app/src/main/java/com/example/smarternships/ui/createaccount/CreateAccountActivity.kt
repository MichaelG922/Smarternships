package com.example.smarternships.ui.createaccount

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import com.example.smarternships.R
import com.example.smarternships.databinding.ActivityRegisterBinding


class CreateAccountActivity : AppCompatActivity() {

    private lateinit var mInternButton: Button
    private lateinit var mEmployerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.create_account)

        mInternButton = findViewById<View>(R.id.intern) as Button
        mEmployerButton = findViewById<View>(R.id.employer) as Button

        mInternButton.setOnClickListener {
            //TODO:launch the proper intern/employer activity (don't have these files made yet)
            startActivity(Intent())
        }

        mEmployerButton.setOnClickListener {
            startActivity(Intent())
        }
    }
}