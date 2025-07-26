package com.crypticsamsara.weather.viewmodel

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crypticsamsara.weather.user.AppNavigation


@Composable

fun WeatherApp() {
   // val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val weatherViewModel: WeatherViewModel = viewModel()
    AppNavigation(authViewModel,weatherViewModel)
}