package com.crypticsamsara.weather.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crypticsamsara.weather.loginactivities.LoginScreen
import com.crypticsamsara.weather.registrationactivities.RegistrationScreen
import com.crypticsamsara.weather.useractivities.ProfileScreen
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.WeatherViewModel


@Composable
/*
fun AppNavigation(viewModel: AuthViewModel,
                  navController: NavHostController
) {
    // Define the screens in the app
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(viewModel,
                { token, userId ->
                    navController.navigate(Screen.DashboardScreen.route)
                },
                { navController.navigate(Screen.Register.route) },
                navController)

        }

        composable(route = Screen.Register.route) {
            RegistrationScreen(viewModel, { token, userId ->
                navController.navigate(Screen.DashboardScreen.route)
            },
                { navController.navigate(Screen.Login.route) },
                navController)
        }

        composable(route = Screen.DashboardScreen.route) {
            DashBoard()
        }

        composable(route = Screen.Settings.route) {
            SettingScreen()
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
    }
} */

fun AppNavigation(viewModel: AuthViewModel,
                  weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    val authToken by viewModel.authToken.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Handle navigation based on auth state
    LaunchedEffect(authToken) {
        if (authToken != null) {
            navController.navigate(Screen.DashboardScreen.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authToken != null)
            Screen.DashboardScreen.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(viewModel = viewModel,
                navController)
        }

        composable(Screen.Register.route) {
            RegistrationScreen(viewModel, navController)
        }

        composable(Screen.DashboardScreen.route) {
            DashBoard(
                viewModel,
                navController,
                weatherViewModel,
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Other screens...
        composable(Screen.Profile.route) {
            ProfileScreen(
                userData = currentUser,
                onBack = { navController.popBackStack() },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                user = currentUser
            )
        }
    }
    }
