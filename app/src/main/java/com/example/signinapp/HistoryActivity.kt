package com.example.signinapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.core.util.Pair
import com.example.signinapp.models.SessionManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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

        val etFrom = findViewById<TextInputEditText>(R.id.etDateFrom)
        val etTo = findViewById<TextInputEditText>(R.id.etDateTo)
        val btnBuscar = findViewById<Button>(R.id.btnBuscarHistory)

        // Preparamos el selector de Rango
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Selecciona rango de fechas")
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        // Función para abrir el calendario al tocar CUALQUIERA de los dos campos
        val openPicker = {
            // Evitamos que se abra dos veces si pulsas rápido
            if (!dateRangePicker.isAdded) {
                dateRangePicker.show(supportFragmentManager, "date_range_picker")
            }
        }

        etFrom.setOnClickListener { openPicker() }
        etTo.setOnClickListener { openPicker() }

        // AL SELECCIONAR FECHAS:
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            // selection es un Pair<Long, Long> con las fechas en milisegundos
            val startDate = selection.first
            val endDate = selection.second

            // Formatear bonito para mostrar en los inputs
            etFrom.setText(formatDate(startDate))
            etTo.setText(formatDate(endDate))

            // Aquí podrías guardar las fechas en variables para usarlas luego en la API
        }

        btnBuscar.setOnClickListener {
            // Tu lógica para llamar a la API usando etFrom.text y etTo.text
        }

        btnBack.setOnClickListener { finish() }
    }

    // Función auxiliar para convertir Milisegundos a String "YYYY-MM-DD"
    private fun formatDate(timestamp: Long): String {
        // Importante: MaterialDatePicker usa UTC, así que forzamos UTC para no tener desfases de días
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(timestamp))
    }

}
