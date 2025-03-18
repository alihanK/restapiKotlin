package com.package.socialapp.uiscreens.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.package.socialapp.repository.UserRepository
import com.package.socialapp.uiscreens.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var userSettings by mutableStateOf(UserSettings())
        private set

    fun loadUserSettings(userId: String) {
        viewModelScope.launch {
            userSettings = userRepository.getUserSettings(userId) ?: UserSettings()
        }
    }

    // Yeni: İsim ve cinsiyeti tek seferde güncelleyen fonksiyon
    fun updateUserSettings(name: String, gender: String, userId: String) {
        viewModelScope.launch {
            val newSettings = UserSettings(userName = name, gender = gender)
            userRepository.updateUserSettings(userId, newSettings)
            userSettings = newSettings
        }
    }
}
