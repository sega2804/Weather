package com.crypticsamsara.weather.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crypticsamsara.weather.language.LanguageScreen
import com.crypticsamsara.weather.loginactivities.LoginScreen
import com.crypticsamsara.weather.registrationactivities.RegistrationScreen
import com.crypticsamsara.weather.useractivities.ProfileScreen
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.WeatherViewModel


@Composable

fun AppNavigation(viewModel: AuthViewModel = hiltViewModel(),
                  weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authToken by viewModel.authToken.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Handle navigation based on auth state
    LaunchedEffect(authToken, currentUser) {
        if (authToken != null && currentUser != null) {
            navController.navigate(Screen.DashboardScreen.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                popUpTo(Screen.Register.route) { inclusive = true }
                popUpTo(Screen.Language.route) { inclusive = true }

            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authToken != null && currentUser != null)
            Screen.DashboardScreen.route else Screen.Splash.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.Register.route) {
            RegistrationScreen(viewModel = viewModel,
                navController = navController)
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

        composable(Screen.Splash.route) {
            SplashScreen(navController)
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
        composable(Screen.Language.route) {
            LanguageScreen(
                viewModel = viewModel,
                navController = navController,
            )

        }
    }
}
