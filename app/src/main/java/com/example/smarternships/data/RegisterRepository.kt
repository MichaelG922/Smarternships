package com.example.smarternships.data

import com.example.smarternships.data.model.LoggedInUser
import com.example.smarternships.data.model.RegisteredUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class RegisterRepository(val dataSource: RegisterDataSource) {

    // in-memory cache of the loggedInUser object
    var user: RegisteredUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun register(username: String, password: String, confirmPass: String): Result<RegisteredUser> {
        // handle login
        val result = dataSource.register(username, password,confirmPass)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(newUser: RegisteredUser) {
        this.user = newUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}