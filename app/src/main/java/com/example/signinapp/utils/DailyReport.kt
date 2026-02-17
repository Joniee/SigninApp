package com.example.signinapp.utils

import com.example.signinapp.models.AccessLog


data class DailyReport(
    val date: String,
    val events: List<AccessLog>
)
