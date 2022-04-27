package com.example.smarternships.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.smarternships.R
import com.example.smarternships.data.RegisterRepository
import com.example.smarternships.data.Result
import com.example.smarternships.ui.login.LoggedInUserView

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(username: String, password: String, confirmPass: String) {
        // can be launched in a separate asynchronous job
        val result = registerRepository.register(username, password,confirmPass)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.reg_failed)
        }
    }

    fun registerDataChanged(username: String, password: String, confirmPass: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if(!passMatch(password,confirmPass)){
            _registerForm.value = RegisterFormState(confPassError = R.string.invalid_conf)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password != null && password.length >= 5
    }

    private fun passMatch(password:String, confirmPass:String) : Boolean {
        return password == confirmPass
    }
}