package com.example.smarternships.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R
import com.example.smarternships.data.model.DataBase

class ViewAccount: AppCompatActivity() {
    private lateinit var mTextName: EditText
    private lateinit var mSaveButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_account)

        mTextName = findViewById<View>(R.id.text_name) as EditText
        mSaveButton = findViewById<View>(R.id.save_button) as Button

        mSaveButton.setOnClickListener {
            var name = mTextName.text.toString()
            Toast.makeText(applicationContext, "Save Name $name", Toast.LENGTH_SHORT).show()
            DataBase.setUserName("1", name)
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
            R.id.action_settings -> {
                Toast.makeText(applicationContext, "Redirect to Settings", Toast.LENGTH_SHORT).show()
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

