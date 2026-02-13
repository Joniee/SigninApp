package com.example.signinapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.signinapp.models.*
import com.example.signinapp.api.RetrofitClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*



class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)

        val tvName = findViewById<TextView>(R.id.tvNameProfile)
        val tvId = findViewById<TextView>(R.id.tvIdProfile)

        val user = SessionManager.currentUser

        if(user != null) {
            tvName.text = user.name
            tvId.text = "ID: ${user.id}"
        }



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
            val startDateStr = etFrom.text.toString()
            val endDateStr = etTo.text.toString()

            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                Toast.makeText(this, "Select dates first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performSearch(startDateStr, endDateStr, rvHistory, btnBuscar)
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

    private fun performSearch(startDate: String, endDate: String, recyclerView: RecyclerView, button: Button) {
        val user = SessionManager.currentUser ?: return

        lifecycleScope.launch {
            try {
                button.isEnabled = false
                button.text = "Buscando..."

                // Convert UI format (dd/MM/yyyy) to API format (yyyy-MM-dd)
                val apiStartDate = convertToIsoDate(startDate)
                val apiEndDate = convertToIsoDate(endDate)

                val request = HistoryRequest(user.id, apiStartDate, apiEndDate)
                val response = RetrofitClient.apiService.getHistory(request)
                val adapter = HistoryAdapter()

                if (response.isSuccessful && response.body() != null) {
                    val rawLogs = response.body()?.results!!

                    val reports = adapter.updateData(rawLogs)

                    recyclerView.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    recyclerView.adapter = adapter


                } else if(response.code() == 404){
                    Toast.makeText(this@HistoryActivity, "No hay actividad en este rango", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@HistoryActivity, "Server error", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@HistoryActivity, "Connection error", Toast.LENGTH_SHORT).show()
            } finally {
                button.isEnabled = true
                button.text = "Buscar"
            }
        }
    }

    // Helper: Converts UI string (dd/MM/yyyy) to API string (yyyy-MM-dd)
    private fun convertToIsoDate(uiDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(uiDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            uiDate // Fallback
        }
    }

}
