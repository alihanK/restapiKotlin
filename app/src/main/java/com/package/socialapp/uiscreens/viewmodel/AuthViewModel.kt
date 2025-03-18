package com.package.socialapp.uiscreens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.package.socialapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.getCurrentUser())
    val user: StateFlow<FirebaseUser?> = _user.asStateFlow()

    private val _authState = MutableStateFlow<String?>(null)
    val authState: StateFlow<String?> = _authState.asStateFlow()

    fun register(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Log.d("Register", "Email veya şifre boş")
            return
        }
        authRepository.registerWithEmail(email, password) { success, message ->
            if (success) {
                _user.value = authRepository.getCurrentUser()
            } else {
                Log.e("Register", "Kayıt hatası: $message")
            }
            _authState.value = message
        }
    }

    fun login(email: String, password: String) {
        authRepository.loginWithEmail(email, password) { success, message ->
            if (success) {
                _user.value = authRepository.getCurrentUser()
            }
            _authState.value = message
        }
    }

    fun loginWithGoogle(idToken: String) {
        authRepository.firebaseAuthWithGoogle(idToken) { success, message ->
            if (success) {
                _user.value = authRepository.getCurrentUser()
            }
            _authState.value = message
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }

    fun deleteAccount() {
        authRepository.deleteAccount { success, message ->
            if (success) {
                signOut()
            } else {
                _authState.value = message
            }
        }
    }
}
