package com.example.signinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.signinapp.models.SessionManager

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnFichar = findViewById<Button>(R.id.btnFichar)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Recuperar datos del Login
        val userId = intent.getIntExtra("USER_ID", 0)
        val userName = intent.getStringExtra("USER_NAME") ?: "Usuario"

        tvWelcome.text = "Hola, $userName"

        btnHistory.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@DashboardActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        btnPerfil.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MainActivity::class.java)
            SessionManager.logout()
            startActivity(intent)
        }

        btnFichar.setOnClickListener {
            // AQUÍ PONDREMOS EL CÓDIGO DEL GPS Y LA API DE FICHAJE
            // Por ahora probamos que el botón funcione
            tvStatus.text = "Procesando fichaje..."
            Toast.makeText(this, "Botón pulsado. ID: $userId", Toast.LENGTH_SHORT).show()

            // Cuando me digas que el login funciona, pegamos aquí el código de fichaje real.
        }
    }
}