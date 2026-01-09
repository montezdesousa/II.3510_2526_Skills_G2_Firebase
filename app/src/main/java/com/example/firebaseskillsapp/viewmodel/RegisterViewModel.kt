package com.example.firebaseskillsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String) {
        _registerState.value = RegisterState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registerState.value = RegisterState.Success(auth.currentUser?.email ?: "")
                } else {
                    _registerState.value = RegisterState.Error(task.exception?.localizedMessage ?: "Registration failed")
                }
            }
    }

    fun setError(message: String) {
        _registerState.value = RegisterState.Error(message)
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val email: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}