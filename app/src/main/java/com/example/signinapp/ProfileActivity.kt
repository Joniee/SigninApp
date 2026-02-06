package com.example.signinapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.signinapp.models.SessionManager

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Referencias
        val tvNombre = findViewById<TextView>(R.id.tvNombreProfile)
        val tvId = findViewById<TextView>(R.id.tvIdProfile)
        val tvHorario = findViewById<TextView>(R.id.tvHorario)
        val tvDias = findViewById<TextView>(R.id.tvDias)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // 1. LEEMOS DIRECTAMENTE DE LA SESIÓN
        val usuario = SessionManager.currentUser

        if (usuario != null) {
            tvNombre.text = usuario.name
            tvId.text = "ID: ${usuario.id}"

            val entrada = usuario.scheduleIn ?: "??"
            val salida = usuario.scheduleOut ?: "??"
            tvHorario.text = "$entrada - $salida"

            tvDias.text = "Días: ${usuario.workDays ?: "No definidos"}"
        }

        btnVolver.setOnClickListener { finish() }
    }
}