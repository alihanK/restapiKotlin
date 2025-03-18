package com.package.socialapp.uiscreens.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.package.socialapp.uiscreens.model.UserSettings
import com.package.socialapp.uiscreens.viewmodel.UserSettingsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import com.package.socialapp.uiscreens.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    userId: String,
    userSettingsViewModel: UserSettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        userSettingsViewModel.loadUserSettings(userId)
    }

    val currentSettings: UserSettings = userSettingsViewModel.userSettings

    var name by remember { mutableStateOf(currentSettings.userName) }
    var selectedGender by remember { mutableStateOf(currentSettings.gender) }

    LaunchedEffect(currentSettings) {
        name = currentSettings.userName
        selectedGender = currentSettings.gender
    }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Successfull!") },
            text = { Text("Settings updated!") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.navigate("home") {
                        popUpTo("settings") { inclusive = true }
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("DELETE ACCOUNT") },
            text = { Text("Do you want to delete your account ?") },
            confirmButton = {
                Button(onClick = {
                    authViewModel.deleteAccount()
                    showDeleteDialog = false
                    navController.navigate("register") {
                        popUpTo("settings") { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "BACK"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Gender:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RadioButton(
                    selected = selectedGender.lowercase() == "male",
                    onClick = { selectedGender = "male" }
                )
                Text("Man", modifier = Modifier.padding(top = 12.dp))
                RadioButton(
                    selected = selectedGender.lowercase() == "female",
                    onClick = { selectedGender = "female" }
                )
                Text("Woman", modifier = Modifier.padding(top = 12.dp))
                RadioButton(
                    selected = selectedGender.lowercase() == "non",
                    onClick = { selectedGender = "non" }
                )
                Text("Unspecified", modifier = Modifier.padding(top = 12.dp))
            }
            Button(
                onClick = {
                    // Hem isim hem cinsiyeti tek seferde güncelliyoruz.
                    userSettingsViewModel.updateUserSettings(name, selectedGender, userId)
                    showSuccessDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { showDeleteDialog = true },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete my account", color = Color.White)
            }
        }
    }
}
