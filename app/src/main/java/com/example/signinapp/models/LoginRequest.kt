package com.example.signinapp.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("id") val id: Int,
    @SerializedName("contrasena") val password: String
)
