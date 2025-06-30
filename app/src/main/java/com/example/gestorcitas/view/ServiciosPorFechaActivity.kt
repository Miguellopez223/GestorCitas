package com.example.gestorcitas.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestorcitas.R
import com.example.gestorcitas.controller.ApiClient
import com.example.gestorcitas.controller.ApiService
import com.example.gestorcitas.model.ServicioCita
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiciosPorFechaActivity : AppCompatActivity() {

    private val lista = mutableListOf<ServicioCita>()
    private lateinit var adapter: ServicioCitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_por_fecha)

        val etInicio = findViewById<EditText>(R.id.etInicio)
        val etFin = findViewById<EditText>(R.id.etFin)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val recycler = findViewById<RecyclerView>(R.id.recyclerServicios)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnVolver.setOnClickListener {
            finish()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ServicioCitaAdapter(lista)
        recycler.adapter = adapter

        btnBuscar.setOnClickListener {
            val inicio = etInicio.text.toString().trim()
            val fin = etFin.text.toString().trim()

            if (inicio.isBlank() || fin.isBlank()) {
                Toast.makeText(this, "Ingresá ambas fechas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.retrofit.create(ApiService::class.java)
            api.listarServiciosPorFecha(inicio, fin).enqueue(object : Callback<List<ServicioCita>> {
                override fun onResponse(call: Call<List<ServicioCita>>, response: Response<List<ServicioCita>>) {
                    lista.clear()
                    response.body()?.let { lista.addAll(it) }
                    adapter.notifyDataSetChanged()
                    tvTotal.text = "Total: ${lista.size} citas"
                }

                override fun onFailure(call: Call<List<ServicioCita>>, t: Throwable) {
                    Toast.makeText(this@ServiciosPorFechaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
