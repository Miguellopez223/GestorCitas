package com.example.gestorcitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.Cita
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCitasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitaAdapter
    private val listaCitas = mutableListOf<Cita>()

    private val pacienteId = 1 // Temporal: ID fijo para pruebas



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_citas)

        recyclerView = findViewById(R.id.recyclerCitas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CitaAdapter(listaCitas)
        recyclerView.adapter = adapter

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }


        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.listarCitasPaciente(pacienteId).enqueue(object : Callback<List<Cita>> {
            override fun onResponse(call: Call<List<Cita>>, response: Response<List<Cita>>) {
                if (response.isSuccessful) {
                    listaCitas.clear()
                    response.body()?.let { listaCitas.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@VerCitasActivity, "Error al obtener las citas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cita>>, t: Throwable) {
                Toast.makeText(this@VerCitasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
