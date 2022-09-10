package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {

    val firebaseAuth = FirebaseAuth.getInstance()

    val authenticationListner = FirebaseAuth.AuthStateListener { firebaseAuth ->

        value = firebaseAuth.currentUser
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener { authenticationListner }
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener { authenticationListner }
    }
}
