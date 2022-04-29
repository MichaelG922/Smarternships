package com.example.smarternships.ui.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarternships.R

class ViewAccount: AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_account)
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

