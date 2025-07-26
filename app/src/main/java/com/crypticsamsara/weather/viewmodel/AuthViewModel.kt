package com.crypticsamsara.weather.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypticsamsara.weather.api.AuthApiService
import com.crypticsamsara.weather.loginactivities.LoginRequest
import com.crypticsamsara.weather.loginactivities.LoginResponse
import com.crypticsamsara.weather.registrationactivities.RegistrationRequest
import com.crypticsamsara.weather.registrationactivities.RegistrationResponse
import com.crypticsamsara.weather.useractivities.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


//@AndroidEntryPoint
@HiltViewModel
    class AuthViewModel @Inject constructor
    (private val api: AuthApiService,
            private val prefs: SharedPreferences) : ViewModel() {
    /*
    init {
        _authToken.value = prefs.getString("auth_token", null)
        if (_authToken.value != null) {
            fetchUserProfile()  // Auto-fetch on init if token exists
        }
    }

    // In handleAuthResponse, after setting _authToken:
    prefs.edit().putString("auth_token", _authToken.value).apply()



 */
    // Holds the authentication token
    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    // Holds the current user data
    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    // Login State
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    // Registration State

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()


    // Current language
    private val _language = MutableStateFlow(NigerianLanguage.ENGLISH)
    val language: StateFlow<NigerianLanguage> = _language.asStateFlow()

    // Supported languages list
    private val _supportedLanguages = MutableStateFlow(NigerianLanguage.allLanguages)
    val supportedLanguages: StateFlow<List<NigerianLanguage>> = _supportedLanguages.asStateFlow()

    init {
        _authToken.value = prefs.getString("auth_token", null)
        if (_authToken.value != null) {
            Log.d("AuthVM", "Restoring token: ${_authToken.value}")
            fetchUserProfile()
        }
    }

    // Nigerian languages enum with codes and display names

    enum class NigerianLanguage(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val description: String,
        val flagEmoji: String
    ) {
        ENGLISH(
            "en-US", "English (US)",
            "English", "Get weather info and farming advice in English", "🇺🇸"
        ),

        YORUBA(
            "yo-NG",
            "Yoruba",
            "Èdè Yorùbá",
            "Gba àlàyé ojú-ọjọ́ àti ìmọ̀ràn ọ̀gbìn nínú èdè Yorùbá",
            "🇳🇬"
        ),
        IGBO(
            "ig-NG",
            "Igbo",
            "Asụsụ Igbo",
            "Nweta ọhụụ ihu igwe na ndụmọdụ ọrụ ugbo n'asụsụ Igbo",
            "🇳🇬"
        ),
        HAUSA(
            "ha-NG",
            "Hausa",
            "Harshen Hausa",
            "Samu bayanin yanayi da shawarar noma a cikin harshen Hausa",
            "🇳🇬"
        ),
        NIGERIANPIDGIN(
            "pg-NG",
            "Nigerian Pidgin",
            "Nigerian Pidgin",
            "Get weather info and farming advice for Nigerian Pidgin",
            "🇳🇬"
        );

        companion object {
            val allLanguages = listOf(
                ENGLISH,
                YORUBA,
                IGBO,
                HAUSA,
                NIGERIANPIDGIN
            )

            fun fromCode(code: String): NigerianLanguage {
                return allLanguages.firstOrNull { it.code == code } ?: ENGLISH
            }
        }
    }


    // Registration function
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        state: String
    ) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank()) {
            _registerState.value = AuthState.Error("All fields are required")
            return
        }

        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = api.registerUser(
                    RegistrationRequest(firstName, lastName, email, phoneNumber, password, state)
                )
                /*
                if (response.isSuccessful) {
                    _registerState.value = AuthState.Success("Registration successful")
                } else {
                    _registerState.value = AuthState.Error(
                        response.errorBody()?.string() ?: "Registration failed"
                    )
                }
            } catch (e: Exception) {
                _registerState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
                */
                handleAuthResponse(response, _registerState)
            } catch (e: Exception) {
                Log.e("AuthVM", "Register error: ${e.message}", e)
                _registerState.value =
                    AuthState.Error(getLocalizedErrorMessage(e.localizedMessage ?: "Network error"))
            }
        }
    }

    // Login function
    fun login(email: String, phoneNumber: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = AuthState.Error("Email and password are required")
            return
        }

        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = api.loginUser(
                    LoginRequest(email, phoneNumber, password)
                )
                /*
                if (response.isSuccessful) {
                    _loginState.value = AuthState.Success("Login successful")
                } else {
                    _loginState.value = AuthState.Error(
                        response.errorBody()?.string() ?: "Login failed"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

                 */
                handleAuthResponse(response, _loginState)
            } catch (e: Exception) {
                Log.e("AuthVM", "Login error: ${e.message}", e)
                _loginState.value =
                    AuthState.Error(getLocalizedErrorMessage(e.localizedMessage ?: "Network error"))
            }
        }
    }

    // Function to set the language
    fun setLanguage(languageCode: String) {
        // To save the preference to persistent storage
        _language.value = NigerianLanguage.fromCode(languageCode)
    }

    // Translation functions
    private fun getLocalizedErrorMessage(message: String): String {
        return when (_language.value) {
            NigerianLanguage.HAUSA -> translateToHausa(message)
            NigerianLanguage.YORUBA -> translateToYoruba(message)
            NigerianLanguage.IGBO -> translateToIgbo(message)
            NigerianLanguage.NIGERIANPIDGIN -> translateToPidgin(message)
            else -> message // Default to English
        }
    }

    // Simplified translation functions - in a real app, use string resources
    private fun translateToHausa(message: String): String {
        return when (message) {
            "All fields are required" -> "Ana buƙatar duk filayen"
            "Email and password are required" -> "Ana buƙatar imel da kalmar sirri"
            "Registration successful" -> "Rajista ta yi nasara"
            "Login successful" -> "Shiga ta yi nasara"
            else -> message
        }
    }

    private fun translateToYoruba(message: String): String {
        return when (message) {
            "All fields are required" -> "Gbogbo awọn aaye ni a nilo"
            "Email and password are required" -> "A nilo imeeli ati ọrọ igbaniwọle"
            "Registration successful" -> "Iforukọsilẹ aṣeyọri"
            "Login successful" -> "Wọle ni aṣeyọri"
            else -> message
        }
    }

    private fun translateToIgbo(message: String): String {
        return when (message) {
            "All fields are required" -> "Achọrọ ubi niile"
            "Email and password are required" -> "Achọrọ email na paswọọdụ"
            "Registration successful" -> "Ndebanye aha gara nke ọma"
            "Login successful" -> "Nbanye gara nke ọma"
            else -> message
        }
    }

    private fun translateToPidgin(message: String): String {
        return when (message) {
            "All fields are required" -> "We need all di information"
            "Email and password are required" -> "We need your email and password"
            "Registration successful" -> "Don don register"
            "Login successful" -> "Don enter"
            else -> message
        }
    }

    /*
    private fun handleAuthResponse(response: Response<*>, stateFlow: MutableStateFlow<AuthState>) {
        if (response.isSuccessful) {
            when (val body = response.body()) {
                is LoginResponse -> {
                    body.data?.token?.let { _authToken.value = it }
                    stateFlow.value =
                        AuthState.Success(getLocalizedErrorMessage("Login successful"))
                    fetchUserProfile()
                }

                is RegistrationResponse -> {
                    body.data?.token?.let { _authToken.value = it }
                    stateFlow.value =
                        AuthState.Success(getLocalizedErrorMessage("Registration successful"))
                    fetchUserProfile()
                }

                else -> stateFlow.value =
                    AuthState.Error(getLocalizedErrorMessage("Unexpected response"))
            }
        } else {
            stateFlow.value = AuthState.Error(
                getLocalizedErrorMessage(
                    response.errorBody()?.string() ?: "Request failed"
                )
            )
        }
    }

 */

    private fun handleAuthResponse(response: Response<*>, stateFlow: MutableStateFlow<AuthState>) {
        if (response.isSuccessful) {
            when (val body = response.body()) {
                is LoginResponse -> {
                    body.data?.token?.let {
                        _authToken.value = it
                        prefs.edit().putString("auth_token", it).apply()
                        Log.d("AuthVM", "Login successful, token saved: $it")
                    }
                    stateFlow.value =
                        AuthState.Success(getLocalizedErrorMessage("Login successful"))
                    fetchUserProfile()
                }

                is RegistrationResponse -> {
                    body.data?.token?.let {
                        _authToken.value = it
                        prefs.edit().putString("auth_token", it).apply()
                        Log.d("AuthVM", "Registration successful, token saved: $it")
                    }
                    stateFlow.value =
                        AuthState.Success(getLocalizedErrorMessage("Registration successful"))
                    fetchUserProfile()
                }

                else -> {
                    Log.e("AuthVM", "Unexpected response: $body")
                    stateFlow.value =
                        AuthState.Error(getLocalizedErrorMessage("Unexpected response"))
                }
            }
        } else {
            val error = response.errorBody()?.string() ?: "Request failed"
            Log.e("AuthVM", "API error: $error")
            stateFlow.value = AuthState.Error(getLocalizedErrorMessage(error))
        }
    }


    /*

private fun fetchUserProfile() {
    viewModelScope.launch {
        _authToken.value?.let { token ->
            try {
                val response = api.getCurrentUser("Bearer $token")
                if (response.isSuccessful) {
                    _currentUser.value = response.body()?.data
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

 */

    private fun fetchUserProfile() {
        _authToken.value?.let { token ->
            viewModelScope.launch {
                try {
                    val response = api.getCurrentUser("Bearer $token")
                    if (response.isSuccessful) {
                        _currentUser.value = response.body()?.data
                        Log.d("AuthVM", "User fetched: ${_currentUser.value}")
                    } else {
                        Log.e("AuthVM", "User fetch failed: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("AuthVM", "User fetch error: ${e.message}", e)
                }
            }
        }
    }


    fun logout() {
        _authToken.value = null
        _currentUser.value = null
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
        _language.value = NigerianLanguage.ENGLISH
        prefs.edit().remove("auth_token").apply()
        Log.d("AuthVM", "Logged out, token cleared")
    }

}
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }



// This ViewModel handles user authentication, including registration and login.
// It uses a Retrofit API service to communicate with the backend.
// The ViewModel maintains the authentication state and provides methods to register and login users.