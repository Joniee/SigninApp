package com.example.signinapp.models

import com.example.signinapp.models.WorkerOut

object SessionManager {
    var currentUser: WorkerOut? = null

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    fun logout() {
        currentUser = null
    }
}