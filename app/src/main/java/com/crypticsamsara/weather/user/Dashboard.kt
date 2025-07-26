package com.crypticsamsara.weather.user

import com.crypticsamsara.weather.viewmodel.WeatherViewModel.WeatherState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.crypticsamsara.weather.R
import com.crypticsamsara.weather.viewmodel.AuthViewModel
import com.crypticsamsara.weather.viewmodel.WeatherViewModel
import com.crypticsamsara.weather.weatheractivities.CurrentWeather
import com.crypticsamsara.weather.weatheractivities.DailyWeather
//import com.google.android.gms.awareness.state.Weather

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoard(
    viewModel: AuthViewModel,
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Fetch weather data when screen loads
    LaunchedEffect(Unit) {
        currentUser?.location?.state?.let { state ->
            viewModel.authToken.value?.let { token ->
                weatherViewModel.getWeatherByState(state, token)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weather Forecast") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = Screen.DashboardScreen.route,
                onNavigationSelected = {route ->
                    when (route) {
                        "profile" -> onProfileClick()
                        "settings" -> onSettingsClick()
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            when (weatherState) {
                is WeatherState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is WeatherState.Error -> {
                    Text(
                        text = (weatherState as WeatherState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    weatherData?.let { weather ->
                        // Current Weather Card
                        WeatherCard(weather.current, weather.daily.firstOrNull())

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hourly Forecast
                        Text("Hourly Forecast", style = MaterialTheme.typography.titleMedium)
                        HourlyForecast(weather.daily.take(24))

                        Spacer(modifier = Modifier.height(16.dp))

                        // Daily Forecast
                        Text("7-Day Forecast", style = MaterialTheme.typography.titleMedium)
                        DailyForecast(weather.daily)

                        // Weather Alerts
                        weather.daily.firstOrNull()?.let { today ->
                            if (today.pop > 0.5) {
                                WeatherAlert("Rain expected today")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(current: CurrentWeather, today: DailyWeather?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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
                WeatherDetail("☔", "${today?.pop?.times(100)?.toInt()}%", "Rain")
            }
        }
    }
}

@Composable
fun HourlyForecast(hourly: List<DailyWeather>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
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
fun DailyForecast(daily: List<DailyWeather>) {
    Column {
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
fun WeatherAlert(message: String) {
    Card(
        modifier = Modifier
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
    // Implement time formatting
    return "12 PM"
}

fun formatDate(timestamp: Long): String {
    // Implement date formatting
    return "Mon"
}


@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigationSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            route = "home",
            icon = Icons.Default.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = "forecast",
            icon = Icons.Default.Explore,
            label = "Forecast"
        ),
        BottomNavItem(
            route = "alerts",
            icon = Icons.Default.Notifications,
            label = "Alerts"
        ),
        BottomNavItem(
            route = "profile",
            icon = Icons.Default.Person,
            label = "Profile"
        )
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

