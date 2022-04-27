package com.example.smarternships.ui.createaccount

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.smarternships.databinding.ActivityRegisterBinding

import com.example.smarternships.R
import com.example.smarternships.ui.login.LoggedInUserView
import com.example.smarternships.ui.login.afterTextChanged

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}