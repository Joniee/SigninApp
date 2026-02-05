package com.example.signinapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnFichar = findViewById<Button>(R.id.btnFichar)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Recuperar datos del Login
        val userId = intent.getIntExtra("USER_ID", 0)
        val userName = intent.getStringExtra("USER_NAME") ?: "Usuario"

        tvWelcome.text = "Hola, $userName"

        btnFichar.setOnClickListener {
            // AQUÍ PONDREMOS EL CÓDIGO DEL GPS Y LA API DE FICHAJE
            // Por ahora probamos que el botón funcione
            tvStatus.text = "Procesando fichaje..."
            Toast.makeText(this, "Botón pulsado. ID: $userId", Toast.LENGTH_SHORT).show()

            // Cuando me digas que el login funciona, pegamos aquí el código de fichaje real.
        }
    }
}