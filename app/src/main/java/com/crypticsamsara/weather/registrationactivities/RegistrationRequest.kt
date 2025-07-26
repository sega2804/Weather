package com.crypticsamsara.weather.registrationactivities




data class RegistrationRequest(
    val firstName: String? = null,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val state: String
) {
    fun isValid(): Boolean =
      //  firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isValidEmail() &&
                phoneNumber.length == 11 &&
                        phoneNumber.isValidPhoneNumber() &&
                password.length >= 8 &&
                        password.isValidPassword() &&
                state.isNotBlank()

    private fun String.isValidEmail(): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return matches(emailRegex.toRegex())
    }

    private fun String.isValidPassword(): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,30}$"
       // "^.{8,30}\$"
            return matches(passwordRegex.toRegex())
    }

    private fun String.isValidPhoneNumber(): Boolean{
        val phoneNumberRegex = "^(?:\\+234|0)[789][01]\\d{8}$"
     //   "^\\d{10,15}$"
        return matches(phoneNumberRegex.toRegex())
    }

}