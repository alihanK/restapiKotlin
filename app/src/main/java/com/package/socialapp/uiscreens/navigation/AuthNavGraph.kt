package com.package.socialapp.uiscreens.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.package.socialapp.uiscreens.screens.HomeScreen
import com.package.socialapp.uiscreens.screens.LoginScreen
import com.package.socialapp.uiscreens.screens.RegisterScreen
import com.package.socialapp.uiscreens.screens.SettingsScreen
import com.package.socialapp.uiscreens.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user by authViewModel.user.collectAsState(initial = null)
    NavHost(
        navController = navController,
        startDestination = if (user == null) "login" else "home"
    ) {
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                userId = user?.uid ?: ""
            )
        }
    }
}
