package com.example.smarternships.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarternships.R

class EditAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.edit_account)
    }
}