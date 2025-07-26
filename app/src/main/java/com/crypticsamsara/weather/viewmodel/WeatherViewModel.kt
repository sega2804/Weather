package com.crypticsamsara.weather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypticsamsara.weather.api.AuthApiService
import com.crypticsamsara.weather.weatheractivities.WeatherByCoordinatesRequest
import com.crypticsamsara.weather.weatheractivities.WeatherByStateRequest
import com.crypticsamsara.weather.weatheractivities.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val api: AuthApiService,
  //  private val prefs: SharedPreferences
) : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData.asStateFlow()

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()
// Sealed Class for weather states
    sealed class WeatherState {
        object Idle : WeatherState()
        object Loading : WeatherState()
        object Success : WeatherState()
        data class Error(val message: String) : WeatherState()
    }

    fun getWeatherByState(state: String, token: String) {
        _weatherState.value = WeatherState.Loading
        viewModelScope.launch {
            try {
                // InCase of error
                Log.d("WeatherVM", "Fetching weather for state: $state")
                val response = api.getWeatherByState(
                    WeatherByStateRequest(state),
                    "Bearer $token"
                )
                if (response.isSuccessful) {
                    _weatherData.value = response.body()
                    _weatherState.value = WeatherState.Success
                    // InCase of error
                    Log.d("WeatherVM", "Weather fetched: ${_weatherData.value}")
                } else {
                    // InCase of error
                    val error = response.errorBody()?.string() ?: "Failed to fetch weather"
                    Log.e("WeatherVM", "API error: $error")
                    // Before update
                    _weatherState.value = WeatherState.Error(
                       // response.errorBody()?.string() ?: "Failed to fetch weather"
                        error
                    )
                }
            } catch (e: Exception) {
                // InCase of error
                Log.e("WeatherVM", "Network error: ${e.message}", e)

                // Before update
                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "Network error")
            }
        }
    }

    fun getWeatherByCoordinates(lat: Double, lon: Double, token: String) {
        _weatherState.value = WeatherState.Loading
        viewModelScope.launch {
            try {
                // InCase of error
                Log.d("WeatherVM", "Fetching weather for coordinates: $lat, $lon")

                // Before Update
                val response = api.getWeatherByCoordinates(
                    WeatherByCoordinatesRequest(lat, lon),
                    "Bearer $token"
                )
                if (response.isSuccessful) {
                    _weatherData.value = response.body()
                    _weatherState.value = WeatherState.Success

                    // InCase of error
                    Log.d("WeatherVM", "Weather fetched: ${_weatherData.value}")

                } else {
                    // InCase of error
               val error = response.errorBody()?.string() ?: "Failed to fetch weather"
                    Log.e("WeatherVM", "API error: $error")


                    _weatherState.value = WeatherState.Error(
                        response.errorBody()?.string() ?: "Failed to fetch weather"
                    )
                }
            } catch (e: Exception) {
                // InCase of error
                Log.e("WeatherVM", "Network error: ${e.message}", e)

                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "Network error")
            }
        }
    }
}

