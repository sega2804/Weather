package com.crypticsamsara.weather

// import com.crypticsamsara.weather.api.AuthApiClient
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crypticsamsara.weather.ui.theme.WeatherTheme
import com.crypticsamsara.weather.user.AppNavigation
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.WeatherApp
import com.crypticsamsara.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: AuthViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()

     

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            WeatherTheme {
             /*   val viewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(retrofit)
                )

              */
              //  WeatherApp()
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
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




