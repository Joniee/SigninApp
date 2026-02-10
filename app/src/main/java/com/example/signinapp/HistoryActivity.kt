package com.example.signinapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.signinapp.models.SessionManager

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_story)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        btnHistory.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@HistoryActivity, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnPerfil.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@HistoryActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this@HistoryActivity, MainActivity::class.java)
            SessionManager.logout()
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener { finish() }
    }
}