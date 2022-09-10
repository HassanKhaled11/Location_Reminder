package com.udacity.project4.authentication

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragment

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewmodel: AuthenticationViewmodel

    private var signInLauncher = registerForActivityResult(

        FirebaseAuthUIActivityResultContract()
    ) { res ->
        signInResult(res)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        setContentView(binding.root)

        auth = Firebase.auth

        viewmodel = ViewModelProvider(this).get(AuthenticationViewmodel::class.java)

        binding.loginbutton.setOnClickListener {
            lauchSignInFlow()
        }

        viewmodel.authenticationState.observe(this) {
            if (it == AuthenticationViewmodel.AuthenticationState.AUTHENTICATED) {
                val intent = Intent(this,RemindersActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }
    }

    private fun lauchSignInFlow() {

        val providers  = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

        signInLauncher.launch(signInIntent)

    }


    private fun signInResult(res: FirebaseAuthUIAuthenticationResult?) {

        val response = res?.idpResponse

        if (res?.resultCode == Activity.RESULT_OK){
            val intent = Intent(this,RemindersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        else{
            Log.i("Error login","Sign is unSuccessful${response?.error?.errorCode}")
        }

    }

}