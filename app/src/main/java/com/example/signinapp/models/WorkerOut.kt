package com.example.signinapp.models

data class WorkerOut(
    val id: Int,
    val name: String,
    val is_active: Boolean,
    val is_admin: Boolean,
    val scheduleIn: String,
    val scheduleOut: String,
    val workDays: String
)
