package com.crypticsamsara.weather.viewmodel

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crypticsamsara.weather.user.AppNavigation


@Composable
/*
fun AppEntry(){
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://api.nysc-hackathon.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api = retrofit.create(AuthApiService::class.java)
    val viewModel = remember { AuthViewModel(api) }

    RegistrationScreen(viewModel,
           onRegisterClick  = { firstName, lastName,  email, password, state ->
        viewModel.register(firstName, lastName, email, password, state)
    },
        onLoginClick = {
            // Handle registration click
        },
        navController = rememberNavController()
    )


    LoginScreen(viewModel,
        onLoginClick = { email, phone,  password ->
            viewModel.login(email, phone, password)
        },
        onRegisterClick = {
            // Handle registration click
        },
        navController = rememberNavController()
    )

}*/


fun WeatherApp() {
   // val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val weatherViewModel: WeatherViewModel = viewModel()
    AppNavigation(authViewModel,weatherViewModel)
}
  //val apiService = rememberApiService()
   // val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(apiService))


/*
    // AppNavigation(authViewModel, weatherViewModel)

    // Main app navigation structure
    NavHost(
        navController = navController,
        startDestination = "language"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable("register") {
            RegistrationScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable("dashboard") {
            // Dashboard screen can be implemented here
            // DashboardScreen(viewModel = authViewModel)
            DashBoard(
                 authViewModel,
                navController,
                weatherViewModel,
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }

            )
        }
        composable ("language"){
            LanguageScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable ("profile"){
            ProfileScreen(
                userData = authViewModel.currentUser.value,
                onBack = { navController.popBackStack() },
                onLogout = {}
            )
        }
    }
}



@Composable
private fun rememberApiService(): AuthApiService {
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl(" https://weather-app-backend-fdzb.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    return remember { retrofit.create(AuthApiService::class.java) }
}







class AuthViewModelFactory(
    private val apiService: AuthApiService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

 */