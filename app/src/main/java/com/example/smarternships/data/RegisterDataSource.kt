package com.example.smarternships.data

import com.example.smarternships.data.model.RegisteredUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource {

    fun register(username: String, password: String, confirmPass: String): Result<RegisteredUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = RegisteredUser(java.util.UUID.randomUUID().toString(), " Let's get set up.")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error registering in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}