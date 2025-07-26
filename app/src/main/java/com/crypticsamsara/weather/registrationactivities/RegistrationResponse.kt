package com.crypticsamsara.weather.registrationactivities

import com.google.gson.annotations.SerializedName


data class RegistrationResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AuthData?
)
data class AuthData(

    @SerializedName ("token") val token: String? = null,
    @SerializedName ("firstName") val firstName: String,
    @SerializedName ("lastName") val lastName: String,
    @SerializedName ("email") val email: String,
    @SerializedName ("password") val password: String,
    @SerializedName ("_id") val _id: String,
    @SerializedName ("createdAt") val createdAt: String,
    @SerializedName ("updatedAt") val updatedAt: String,
    @SerializedName ("__v") val __v: Int,
)
