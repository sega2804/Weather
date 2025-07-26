package com.crypticsamsara.weather.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.crypticsamsara.weather.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
  //  viewModel: AuthViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "AgriCast",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "AgriCast Logo",
            modifier = Modifier.size(100.dp)
        )

        CircularProgressIndicator(
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }

    // To simulate initialization and navigate to login after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("language") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

