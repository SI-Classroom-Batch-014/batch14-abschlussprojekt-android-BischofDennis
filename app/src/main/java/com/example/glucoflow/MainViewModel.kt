package com.example.glucoflow

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainViewModel: ViewModel() {
     // Instanz von Firebase Authentication
     // Ersetzt in diesem Fall ein Repository
    // private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuth = Firebase.auth

    fun loginWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    completion()
                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
            }
        }
    }

    fun registerWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    completion()
                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}