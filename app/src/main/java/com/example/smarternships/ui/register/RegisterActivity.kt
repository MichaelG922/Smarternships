package com.example.smarternships.ui.register

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.smarternships.databinding.ActivityRegisterBinding

import com.example.smarternships.R
import com.example.smarternships.ui.account.CreateAccountActivity
import com.example.smarternships.ui.account.ViewAccountActivity
import com.example.smarternships.ui.login.LoggedInUserView
import com.example.smarternships.ui.login.LoginActivity
import com.example.smarternships.ui.login.afterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    //For Firebase authentication
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

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

        password.afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    confirmpass?.text.toString()
                )
            }

            confirmpass!!.afterTextChanged {
                    registerViewModel.registerDataChanged(
                        username.text.toString(),
                        password.text.toString(),
                        confirmpass?.text.toString()
                    )
                }

            create?.setOnClickListener {
                loading.visibility = View.VISIBLE
                val x = mAuth.createUserWithEmailAndPassword(username.text.toString(),password.text.toString())
                x.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(this@RegisterActivity, CreateAccountActivity::class.java)
                        intent.putExtra("USERID", mAuth.currentUser!!.uid)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Error Creating Account Try Again",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(applicationContext, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
//                registerViewModel.register(username.text.toString(), password.text.toString(),confirmpass?.text.toString(),mAuth)
//                val intent_i = Intent(this@RegisterActivity, CreateAccountActivity::class.java)

//                startActivity(intent_i)
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

