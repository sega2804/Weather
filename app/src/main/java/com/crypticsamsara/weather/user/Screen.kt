package com.crypticsamsara.weather.user

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object DashboardScreen : Screen("dashboard")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    object Language: Screen("language")
    object Splash: Screen("splash")
    object Weather: Screen("weather")
    object WeatherByCoordinates: Screen("weather_by_coordinates")
    object WeatherByState: Screen("weather_by_state")
    object WeatherDetails: Screen("weather_details")
    object WeatherForecast: Screen("weather_forecast")
}