package com.example.smarternships.data

import com.example.smarternships.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, mAuth: FirebaseAuth): Result<LoggedInUser> {

        var logInSuccess = false
        // handle login
        mAuth!!.signInWithEmailAndPassword(username,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){

                    logInSuccess = true
                }
            }

        try {
            // TODO: handle loggedInUser authentication
                if(logInSuccess){
                    val user = LoggedInUser(password, username)
                    return Result.Success(user)
                }else{
                    return Result.Error(IOException("Incorrect username or password!"))
                }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}