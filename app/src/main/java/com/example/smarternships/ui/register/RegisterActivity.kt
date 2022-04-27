package com.example.smarternships.ui.register

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.smarternships.databinding.ActivityRegisterBinding

import com.example.smarternships.R
import com.example.smarternships.ui.login.LoggedInUserView
import com.example.smarternships.ui.login.afterTextChanged

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val confirmpass = binding.confirmPass
        val create = binding.create
        val loading = binding.loading

        registerViewModel = ViewModelProvider(this, RegisterModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable login button unless all fields valid
            create?.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if(registerState.confPassError != null){
                confirmpass?.error = getString(registerState.confPassError!!)
            }
        })

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (registerResult.error != null) {
                showLoginFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                updateUiWithUser(registerResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                confirmpass?.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    confirmpass?.text.toString()
                )
            }

            confirmpass.apply {
                afterTextChanged {
                    registerViewModel.registerDataChanged(
                        username.text.toString(),
                        password.text.toString(),
                        confirmpass?.text.toString()
                    )
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            username.text.toString(),
                            password.text.toString(),
                            confirmpass?.text.toString()
                        )
                }
                false
            }

            create?.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(username.text.toString(), password.text.toString(),confirmpass?.text.toString())
                //TODO:continue registration
            }
    }
}

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

