package com.example.signinapp.models

import com.google.gson.annotations.SerializedName

data class AccessLog (
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("geoLocation") val geoLocation: String
)