package com.example.glucoflow

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.glucoflow.data.Profile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class MainViewModel: ViewModel() { //: (application: )
     // Instanz von Firebase Authentication
     // Ersetzt in diesem Fall ein Repository
    // private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuth = Firebase.auth

    //update firestire
    private val db = Firebase.firestore

    private var _currentUser = MutableLiveData<FirebaseUser>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    lateinit var profileRef: DocumentReference

    init {
        if (firebaseAuth.currentUser != null){
            setProfileRef()
        }
    }



    fun loginWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    completion()
                    //
                    firebaseAuth.currentUser?.sendEmailVerification()

                    profileRef = db.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                    profileRef.set(Profile())

                    db.collection("profiles").document(firebaseAuth.currentUser!!.uid).set(Profile())

                    _currentUser.value = firebaseAuth.currentUser
                    profileRef = db.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
            }
        }
    }

    private fun setProfileRef() {

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


    fun updateProfile(profile: Profile){

        profileRef.set(profile)

    }
}