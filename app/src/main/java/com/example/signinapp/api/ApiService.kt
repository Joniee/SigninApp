package com.example.signinapp.api

import com.example.signinapp.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<WorkerOut>

    @POST("access/query")
    suspend fun getHistory(@Body request: HistoryRequest): Response<HistoryResponse>

    @POST("access/create")
    suspend fun checkIn(@Body request: CheckInRequest): Response<Void>
}