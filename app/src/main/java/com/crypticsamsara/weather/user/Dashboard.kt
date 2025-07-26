package com.crypticsamsara.weather.user

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crypticsamsara.weather.R
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.WeatherViewModel
import com.crypticsamsara.weather.weatheractivities.CurrentWeather
import com.crypticsamsara.weather.weatheractivities.DailyWeather
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoard(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val authToken by viewModel.authToken.collectAsState()
    var retryCount by remember { mutableIntStateOf(0) }
    val maxRetries = 3

    // Fetch weather when user and token are available
    LaunchedEffect(currentUser, authToken) {
        val userSnapshot = currentUser
        val tokenSnapshot = authToken
        if (userSnapshot != null && tokenSnapshot != null) {
            userSnapshot.location?.state?.let { state ->
                Log.d("Dashboard", "Fetching weather for state: " +
                        "$state at ${getCurrentTime()}"
                )
                weatherViewModel.getWeatherByState(state, tokenSnapshot)
            }
        }

        // InCase of Error
        while (true) {
            kotlinx.coroutines.delay(60.minutes.inWholeMilliseconds)
            if (userSnapshot != null && tokenSnapshot != null) {
                userSnapshot.location?.state?.let { state ->
                    weatherViewModel.getWeatherByState(state, tokenSnapshot)
                }
            }
        }

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("AgriCast") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Explore, contentDescription = "Settings")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("dashboard") {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Logout")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = Screen.DashboardScreen.route,
                onNavigationSelected = { route ->
                    navController.navigate(route)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (currentUser == null) {
                CircularProgressIndicator()
                Text(
                    "Loading user profile...",
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                when (weatherState) {
                    is WeatherViewModel.WeatherState.Loading -> {
                        CircularProgressIndicator()
                        Text(
                            "Loading weather data...",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    is WeatherViewModel.WeatherState.Error -> {
                        Text(
                            text = "Error: ${(weatherState as WeatherViewModel.WeatherState.Error).message}." +
                                    "Retries left: ${maxRetries - retryCount} ",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                        // InCase of Error
                        if (retryCount < maxRetries) {
                            Button(
                                onClick = {
                                    val userSnapshot = currentUser
                                    val tokenSnapshot = authToken
                                    if (userSnapshot != null && tokenSnapshot != null) {
                                        userSnapshot.location?.state?.let { state ->
                                            weatherViewModel.getWeatherByState(state, tokenSnapshot)
                                            retryCount++
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                    is WeatherViewModel.WeatherState.Idle -> {
                        Text("No weather data loaded yet", modifier = Modifier.fillMaxWidth())

                        // InCase of Error
                        Button(
                            onClick = {
                                val userSnapshot = currentUser
                                val tokenSnapshot = authToken
                                if (userSnapshot != null && tokenSnapshot != null) {
                                    userSnapshot.location?.state?.let { state ->
                                        weatherViewModel.getWeatherByState(state, tokenSnapshot)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text("Load Weather")
                        }
                    }


                    is WeatherViewModel.WeatherState.Success -> {
                        weatherData?.let { data ->
                            WeatherCard(data.current, data.daily.firstOrNull(),
                                modifier = Modifier.weight(1f))

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Hourly Forecast", style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.Start))

                            HourlyForecast(data.daily.take(24),
                                modifier = Modifier.weight(1f))

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("7-Day Forecast", style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.Start))
                            DailyForecast(data.daily,
                                modifier = Modifier.weight(1f))

                            data.daily.firstOrNull()?.let { today ->

                            // iNcASe of Error
                                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                                if (today.pop > 0.5 && currentHour < 18) { // Rain expected before evening
                                    WeatherAlert(

                                        // Change to display alert message and not the predefined below
                                        message = "Rain expected today before 6 PM",
                                        modifier = Modifier.weight(0.5f)
                                    )
                                }
                            }



                        } ?: run {
                            Text("No weather data available",
                                modifier = Modifier.fillMaxWidth())
                            Button(
                                onClick = {
                                    val userSnapshot = currentUser
                                    val tokenSnapshot = authToken
                                    if (userSnapshot != null && tokenSnapshot != null) {
                                        userSnapshot.location?.state?.let { state ->
                                            weatherViewModel.getWeatherByState(state, tokenSnapshot)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(current: CurrentWeather, today: DailyWeather?,
                modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${current.temperature.toInt()}°",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = current.weather.firstOrNull()?.description ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Image(
                    painter = painterResource(id = getWeatherIcon(current.weather.firstOrNull()?.icon)),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetail("🌬️", "${current.windSpeed} km/h", "Wind")
                WeatherDetail("💧", "${current.humidity}%", "Humidity")
                WeatherDetail("☔", "${today?.pop?.times(100)?.toInt() ?: 0}%", "Rain")
            }
        }
    }
}

@Composable
fun HourlyForecast(hourly: List<DailyWeather>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hourly) { hour ->
            HourlyForecastItem(hour)
        }
    }
}

@Composable
fun HourlyForecastItem(hour: DailyWeather) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = formatTime(hour.dt), style = MaterialTheme.typography.labelSmall)
        Image(
            painter = painterResource(id = getWeatherIcon(hour.weather.firstOrNull()?.icon)),
            contentDescription = "Weather icon",
            modifier = Modifier.size(32.dp)
        )
        Text(text = "${hour.temp.day.toInt()}°", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DailyForecast(daily: List<DailyWeather>,
                  modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        daily.forEach { day ->
            DailyForecastItem(day)
        }
    }
}

@Composable
fun DailyForecastItem(day: DailyWeather) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = formatDate(day.dt), style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = getWeatherIcon(day.weather.firstOrNull()?.icon)),
                contentDescription = "Weather icon",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${day.temp.min.toInt()}° / ${day.temp.max.toInt()}°",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun WeatherAlert(message: String,
                 modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alert",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun WeatherDetail(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 24.sp)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

fun getWeatherIcon(iconCode: String?): Int {
    return when (iconCode) {
        "01d" -> R.drawable.ic_clear_day
        "01n" -> R.drawable.ic_clear_night
        "02d", "03d", "04d" -> R.drawable.ic_cloudy_day
        "02n", "03n", "04n" -> R.drawable.ic_cloudy_night
        "09d", "09n", "10d", "10n" -> R.drawable.ic_rain
        "11d", "11n" -> R.drawable.ic_thunderstorm
        "13d", "13n" -> R.drawable.ic_snow
        "50d", "50n" -> R.drawable.ic_fog
        else -> R.drawable.ic_clear_day
    }
}

fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp * 1000))
}

fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("EEE", Locale.getDefault()).format(Date(timestamp * 1000))
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h: mm a", Locale.getDefault())
    return sdf.format(Calendar.getInstance().time)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigationSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("forecast", Icons.Default.Explore, "Forecast"),
        BottomNavItem("alerts", Icons.Default.Notifications, "Alerts"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigationSelected(item.route) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)