package com.example.gestorcitas.view

import android.content.Intent
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

class InsertarPacienteActivity : AppCompatActivity() {

    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_paciente)

        api = ApiClient.retrofit.create(ApiService::class.java)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCi = findViewById<EditText>(R.id.etCi)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPacienteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val ci = etCi.text.toString()
            val telefono = etTelefono.text.toString()

            if (nombre.isNotEmpty() && ci.isNotEmpty() && telefono.isNotEmpty()) {
                api.insertarPaciente(nombre, ci, telefono).enqueue(object : Callback<Respuesta> {
                    override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                        Toast.makeText(this@InsertarPacienteActivity, response.body()?.mensaje ?: "Sin respuesta", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                        Toast.makeText(this@InsertarPacienteActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Llená todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
