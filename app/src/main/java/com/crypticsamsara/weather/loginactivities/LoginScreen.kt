package com.crypticsamsara.weather.loginactivities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crypticsamsara.weather.viewmodel.AuthState
import com.crypticsamsara.weather.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthState.Success -> {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }

            else -> {}
        }
    }

    // InCase of Error
    LaunchedEffect(email, phoneNumber, password) {
        isValid = email.isValidEmail() && phoneNumber.length == 11
                && password.isValidPassword()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F9FD)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Sign In",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black)

                Spacer(Modifier.height(24.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Phone Field
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number (optional)") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                                null
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = { viewModel.login(email, phoneNumber, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = // InCase of Error
                        isValid &&

                        loginState !is AuthState.Loading

                ) {
                    if (loginState is AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Sign In")
                    }
                }

                // Error/Success Message
                when (loginState) {
                    is AuthState.Error -> {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            (loginState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // InCase of error
                    // For Success
                    is AuthState.Success -> {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            (loginState as AuthState.Success).message,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    else -> {}
                }

                Spacer(Modifier.height(8.dp))

                // Register Navigation
                TextButton(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Don't have an account? Sign Up")
                }
            }
        }
    }
}
    fun String.isValidEmail(): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return matches(emailRegex.toRegex())
    }

    fun String.isValidPassword(): Boolean {
        val passwordRegex =
           // "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,30}$"
        "^.{8,30}$"
        return matches(passwordRegex.toRegex())
    }

    fun String.isValidPhoneNumber(): Boolean {
        val phoneNumberRegex = Regex("^(?:\\+234|0)[789][01]\\d{8}$")
        return matches(phoneNumberRegex.toString().toRegex())
    }
