package com.example.smarternships.data

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.smarternships.data.model.LoggedInUser
import com.example.smarternships.data.model.RegisteredUser
import com.example.smarternships.ui.account.CreateAccountActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource {

    fun register(username: String, password: String, confirmPass: String, mAuth: FirebaseAuth): Result<RegisteredUser> {

        val x = mAuth!!.createUserWithEmailAndPassword(username,password)
        var registerSuccess = false
        x.addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.i("--------------------", "task is successful")
                registerSuccess = true
            }
        }

        try {
            // TODO: handle loggedInUser authentication
            if(registerSuccess){
                val user = RegisteredUser(username, password)
                return Result.Success(user)
            }
            else{
                return Result.Error(IOException(""))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error registering in", e))
        }
    }

}