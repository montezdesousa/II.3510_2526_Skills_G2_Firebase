package com.example.firebaseskillsapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseskillsapp.screens.LoginScreen
import com.example.firebaseskillsapp.screens.SkillsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("skills") {
                    popUpTo("login") { inclusive = true } // remove login from backstack
                }
            })
        }

        composable("skills") {
            SkillsScreen(onLogout = {
                navController.navigate("login") {
                    popUpTo("skills") { inclusive = true }
                }
            })
        }
    }
}
