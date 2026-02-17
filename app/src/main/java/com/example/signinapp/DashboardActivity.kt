package com.example.signinapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.signinapp.api.RetrofitClient
import com.example.signinapp.models.CheckInRequest
import com.example.signinapp.models.HistoryRequest
import com.example.signinapp.models.SessionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


class DashboardActivity : AppCompatActivity() {

    private var type: String = "Entrada"
    private lateinit var fusedLocationClient: FusedLocationProviderClient // Cliente de GPS
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnFichar = findViewById<Button>(R.id.btnFichar)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        val user = SessionManager.currentUser

        // Recuperar datos del Login
        val userId = intent.getIntExtra("USER_ID", 0)
        val userName = user?.name

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



        getStatus()

        btnFichar.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this@DashboardActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this@DashboardActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // NO tenemos permiso: Lo pedimos al usuario
                ActivityCompat.requestPermissions(
                    this@DashboardActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // location puede ser null si el GPS está apagado o el móvil es nuevo
                val lat = location?.latitude ?: 0.0
                val long = location?.longitude ?: 0.0

                val locStr = "$lat $long"


                tvStatus.text = "Procesando fichaje..."
                btnFichar.isEnabled = false
                btnFichar.alpha = 0.5f

                if (type == "Entrada" || type == "Salida") {
                    createAccess(locStr)
                }
            }


            Toast.makeText(this, "Cargando datos", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
   private fun createAccess(location: String){
        lifecycleScope.launch{
            try{

                val btnFichar = findViewById<Button>(R.id.btnFichar)
                val user = SessionManager.currentUser
                val request = CheckInRequest(
                    workerId = user!!.id,
                    type = type,
                    location = location
                )

                val response = RetrofitClient.apiService.checkIn(request)
                if(response.isSuccessful){
                    Toast.makeText(this@DashboardActivity, "Fichaje $type realizada con éxito", Toast.LENGTH_SHORT).show()
                    delay(timeMillis = 1000)
                    getStatus()
                    btnFichar.isEnabled = true
                    btnFichar.alpha = 1.0f
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@DashboardActivity, "Error al fichar, vuelve a iniciar sessión", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e:Exception) {
                Toast.makeText(this@DashboardActivity, "Fallo de red: ${e.message}", Toast.LENGTH_LONG).show()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStatus() {
        lifecycleScope.launch {
            val tvStatus = findViewById<TextView>(R.id.tvStatus)

            try{
                val user = SessionManager.currentUser
                val btnFichar = findViewById<Button>(R.id.btnFichar)

                val request = HistoryRequest(user!!.id, LocalDate.now().toString(), LocalDate.now().toString())
                //val request = HistoryRequest(user!!.id, "2025-09-16", "2025-09-16") //Linea de código para pruebas
                val response = RetrofitClient.apiService.getHistory(request)
                if (response.isSuccessful) {
                    val logs = response.body()?.results
                    val lastLog = logs?.maxByOrNull { it.time }
                    if (lastLog != null && lastLog.type == "Entrada") {
                        tvStatus.text = "Estado: Trabajando desde ${lastLog.time}"
                        btnFichar.text = "Registrar salida"
                        type = "Salida"
                    } else {
                        tvStatus.text = "Estado: Actividad finalizada"
                        btnFichar.text = "Registrar entrada"
                        type = "Entrada"
                    }
                } else if(response.code() == 404){
                    tvStatus.text = "Estado: Sin actividad"
                    btnFichar.text = "Registrar entrada"
                    type = "Entrada"
                } else {
                    btnFichar.isEnabled = false
                    btnFichar.text = "Contactar con administrador"
                    btnFichar.alpha = 0.5f
                }
            } catch (e: Exception) {
                // Por seguridad, dejar el botón en "Entrada" o deshabilitarlo
                tvStatus.text = "Estado: Error Desconocido"
            }
        }
    }
}

