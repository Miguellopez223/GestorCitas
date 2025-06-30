package com.example.gestorcitas.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Respuesta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarMedicoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_medico)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEspecialidad = findViewById<EditText>(R.id.etEspecialidad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val api = ApiClient.retrofit.create(ApiService::class.java)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val especialidad = etEspecialidad.text.toString()

            if (nombre.isNotEmpty() && especialidad.isNotEmpty()) {
                api.insertarMedico(nombre, especialidad).enqueue(object : Callback<Respuesta> {
                    override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                        Toast.makeText(this@InsertarMedicoActivity, response.body()?.mensaje ?: "Sin respuesta", Toast.LENGTH_SHORT).show()
                        etNombre.text.clear()
                        etEspecialidad.text.clear()
                    }

                    override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                        Toast.makeText(this@InsertarMedicoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Llená todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
