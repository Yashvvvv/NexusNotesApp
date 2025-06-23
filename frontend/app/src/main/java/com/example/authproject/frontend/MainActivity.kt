package com.example.authproject.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authproject.frontend.ui.screens.login.LoginScreen
import com.example.authproject.frontend.ui.screens.notes.NotesScreen
import com.example.authproject.frontend.ui.screens.register.RegisterScreen
import com.example.authproject.frontend.ui.theme.FrontendTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("notes") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("notes") {
                            NotesScreen(
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("notes") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}