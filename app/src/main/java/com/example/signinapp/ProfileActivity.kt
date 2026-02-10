package com.example.signinapp

import android.content.Intent
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
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val tvDias = findViewById<TextView>(R.id.tvDias)


        btnHistory.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@ProfileActivity, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnPerfil.setOnClickListener {
            // Ir al perfil
            val intent = Intent(this@ProfileActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            SessionManager.logout()
            startActivity(intent)
            finish()
        }

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

        btnBack.setOnClickListener { finish() }
    }
}