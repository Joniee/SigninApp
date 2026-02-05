package com.example.signinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.signinapp.api.RetrofitClient // Asegúrate de importar tu RetrofitClient
import com.example.signinapp.models.LoginRequest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etId = findViewById<EditText>(R.id.etId)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val idTexto = etId.text.toString()
            val pass = etPassword.text.toString()

            if (idTexto.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamamos a la API en segundo plano
            lifecycleScope.launch {
                try {
                    val id = idTexto.toInt() // Convertimos texto a número
                    val request = LoginRequest(id = id, password = pass)

                    // LLAMADA AL SERVIDOR
                    val response = RetrofitClient.apiService.login(request)

                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!

                        // Si todo va bien, saltamos al Dashboard
                        Toast.makeText(this@MainActivity, "Hola ${usuario.name}", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", usuario.id)
                        intent.putExtra("USER_NAME", usuario.name)
                        startActivity(intent)
                        finish() // Cierra la pantalla de login para no volver atrás

                    } else {
                        Toast.makeText(this@MainActivity, "Error: Credenciales incorrectas", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    // Si no hay internet o el servidor está caído
                    Toast.makeText(this@MainActivity, "Fallo de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }
}