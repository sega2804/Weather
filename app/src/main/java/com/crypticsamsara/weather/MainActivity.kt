package com.crypticsamsara.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
// import com.crypticsamsara.weather.api.AuthApiClient
import com.crypticsamsara.weather.api.AuthApiService
import com.crypticsamsara.weather.ui.theme.WeatherTheme
import com.crypticsamsara.weather.user.AppNavigation
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.AuthViewModelFactory
import com.crypticsamsara.weather.viewmodel.WeatherApp
import com.crypticsamsara.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*
    private val viewModel: AuthViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*
        // Retrofit + ViewModel Setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://weather-app-backend-fdzb.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)

         */

        setContent {
            WeatherTheme {
             /*   val viewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(retrofit)
                )

              */
                WeatherApp()
            }
        }

    }
}
/*
@Composable
fun WeatherApp() {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(apiService = AuthApiClient.apiService))
    val weatherViewModel: WeatherViewModel = viewModel(factory = AuthViewModelFactory(apiService = AuthApiClient.apiService ) )

    AppNavigation(authViewModel, weatherViewModel)
}

 */

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WeatherTheme {
        WeatherApp(
        )
    }
}




