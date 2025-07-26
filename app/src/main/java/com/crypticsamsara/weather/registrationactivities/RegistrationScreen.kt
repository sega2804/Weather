package com.crypticsamsara.weather.registrationactivities

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
/*
fun RegistrationScreen(viewModel: AuthViewModel,
                       onRegisterClick: (String, String, String, String
                               , String) -> Unit,
                       onLoginClick: () -> Unit,
                       navController: NavHostController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.isRegistrationSuccessful) {
        if (viewModel.isRegistrationSuccessful) {
            navController.navigate("dashboard") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    // Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F9FD)),
        contentAlignment = Alignment.Center
    ) {
        // CardView
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {


            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Register", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                // Spacer
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                //Spacer
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth())

                // Spacer
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth())


                // Spacer
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                //Spacer
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth())

                // Spacer
                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.register(firstName, lastName, email, password, state)
                }) {
                    Text("Register")
                }

                viewModel.message?.let {
                    Spacer(Modifier.height(16.dp))
                    Text(it, color = Color.Green)
                }

                TextButton(
                    onClick =
                        onLoginClick) {
                    Text("Already have an account? Login")
                }

            }

        }
    }
}*/
fun RegistrationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }

    val registrationState by viewModel.registerState.collectAsState()

    LaunchedEffect(registrationState) {
        if (registrationState is AuthState.Success) {
            navController.navigate("dashboard") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

  /*  Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) */

    // InCase of error
    LaunchedEffect(firstName, lastName, email, phoneNumber, password, state) {
        isValid = firstName.isNotBlank() && lastName.isNotBlank() && email.isValidEmail() &&
                phoneNumber.isValidPhoneNumber() && password.isValidPassword() && state.isNotBlank()
    }



    // Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F9FD)),
        contentAlignment = Alignment.Center
    ) {
        // CardView
        Card(
            /*
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()

             */
            // InCase of Error
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium

        ) {

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sign Up",
                    // fontSize = 24.sp, fontWeight = FontWeight.Bold
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,

                )

                Spacer(modifier = Modifier.height(24.dp))

                // First Name Field
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    singleLine = true,
                    isError = firstName.isNotBlank() && firstName.length < 2,
                    supportingText = {
                        if (firstName.isNotBlank() && firstName.length < 2) {
                            Text("Minimum 2 characters")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

// Spacer
                Spacer(modifier = Modifier.height(16.dp))

                // Last Name Field
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    singleLine = true,
                    isError = lastName.isNotBlank() && lastName.length < 2,
                    modifier = Modifier.fillMaxWidth()
                )

// Spacer
                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = email.isNotBlank() && !email.isValidEmail(),
                    supportingText = {
                        if (email.isNotBlank() && !email.isValidEmail()) {
                            Text("Invalid email format")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer
                Spacer(modifier = Modifier.height(16.dp))

                // phoneNumber Field
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneNumber.isNotBlank() && phoneNumber.length < 11 || phoneNumber.length > 11,
                    supportingText = {
                        if (phoneNumber.isNotBlank() && phoneNumber.length < 11 || phoneNumber.length > 11) {
                            Text("Required 11 characters")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer
                Spacer(modifier = Modifier.height(16.dp))

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
                    isError = password.isNotBlank() && password.length < 8,
                    supportingText = {
                        if (password.isNotBlank() && password.length < 8) {
                            Text("Minimum 8 characters, must include" +
                                    " uppercase, lowercase, number, and special character")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer
                Spacer(modifier = Modifier.height(16.dp))

                // State Field
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer
                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                Button(
                    onClick = {
                        viewModel.register(firstName, lastName, email, phoneNumber,password, state)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registrationState !is AuthState.Loading
                            //&&
                       /*     firstName.isNotBlank() &&
                            lastName.isNotBlank() &&
                            email.isValidEmail() &&
                            phoneNumber.length == 11 &&
                            phoneNumber.isValidPhoneNumber() &&
                            password.length >= 8 &&
                            password.isValidPhoneNumber() &&
                            state.isNotBlank()

                        */
                ) {
                    if (registrationState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Sign Up")
                    }
                }

                // Status Message
                when (registrationState) {
                    is AuthState.Error -> {

                        // InCase of Error
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = (registrationState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            // InCase of Error
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    is AuthState.Success -> {
                        Text(
                            text = (registrationState as AuthState.Success).message,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodySmall,
                                    // InCase of Error
                                    modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> {}
                }

                // Login Navigation
                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Already have an account? Sign In")
                }
            }
        }
    }
}


private fun String.isValidEmail(): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    return matches(emailRegex.toRegex())
}

private fun String.isValidPassword(): Boolean {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,30}$"

    return matches(passwordRegex.toRegex())
}

private fun String.isValidPhoneNumber(): Boolean{
    val phoneNumberRegex = "^(?:\\+234|0)[789][01]\\d{8}$"
       // "^d{10,11}$"
    return matches(phoneNumberRegex.toRegex())
}
