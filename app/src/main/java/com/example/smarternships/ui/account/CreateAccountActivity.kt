package com.example.smarternships.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.example.smarternships.R


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
            val intent = Intent(this, CreateAccountDetailsActivity::class.java)
            intent.putExtra("ISINTERN", true)
            startActivity(intent)
        }

        mEmployerButton.setOnClickListener {
            val intent = Intent(this, CreateAccountDetailsActivity::class.java)
            intent.putExtra("ISINTERN", false)
            startActivity(intent)
        }
    }
}