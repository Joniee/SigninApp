package com.example.signinapp.models

import com.google.gson.annotations.SerializedName

data class CheckInRequest(
    @SerializedName("worker_id") val workerId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("geoLocation") val location: String
)
