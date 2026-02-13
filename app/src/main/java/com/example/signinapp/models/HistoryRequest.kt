package com.example.signinapp.models

import com.google.gson.annotations.SerializedName

data class HistoryRequest(
    @SerializedName("worker_id") val workerId: Int,
    @SerializedName("datefrom") val dateFrom: String,
    @SerializedName("dateto") val dateTo: String
)
