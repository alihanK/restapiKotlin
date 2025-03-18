package com.package.socialapp.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun registerWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit)
    fun loginWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit)
    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit)
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun deleteAccount(onResult: (Boolean, String?) -> Unit)
}
